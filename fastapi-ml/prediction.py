# predict.py
import os
import pandas as pd
import joblib
import matplotlib.pyplot as plt
from matplotlib import font_manager, rc
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score

# 원본 데이터 로드 (predict 스크립트에서도 필요)
try:
    df = pd.read_excel('./data/ElecData.xlsx')
    df['ds'] = pd.to_datetime(df['date'].astype(str) + '-01-01')
    print("✅ 원본 데이터 로드 완료.")
except FileNotFoundError:
    print("❌ 오류: './data/ElecData.xlsx' 파일을 찾을 수 없습니다. 데이터 파일을 확인해주세요.")
    exit() # 데이터 파일이 없으면 스크립트 종료
except Exception as e:
    print(f"❌ 오류: 데이터 로드 중 문제가 발생했습니다: {e}")
    exit()

def load_models(region: str):
    """
    선택한 지역의 Prophet 모델을 로드합니다.

    Args:
        region (str): 모델을 로드할 지역 이름.
    Returns:
        tuple: (피해건수 모델, 피해액 모델)
    Raises:
        FileNotFoundError: 해당 지역의 모델 파일이 존재하지 않을 경우.
    """
    count_model_path = f'./models/prophet_model_count_{region}.pkl'
    amount_model_path = f'./models/prophet_model_amount_{region}.pkl'

    if not os.path.exists(count_model_path) or not os.path.exists(amount_model_path):
        raise FileNotFoundError(f"❌ {region} 모델이 존재하지 않습니다. 모델을 먼저 학습해주세요!")

    model_count = joblib.load(count_model_path)
    model_amount = joblib.load(amount_model_path)

    return model_count, model_amount

def predict(region='서울특별시', type_='전기화재', periods=3):
    """
    지정된 지역과 유형에 대해 예측을 수행하고 성능을 평가합니다.

    Args:
        region (str): 예측할 지역 이름.
        type_ (str): 예측할 재해 유형.
        periods (int): 예측할 미래 기간 (년).
    Returns:
        tuple: (예측 결과 데이터 (dict), 피해액 R2, 피해건수 R2)
    """
    # 지역별 Prophet 모델 로드
    try:
        model_count, model_amount = load_models(region)
    except FileNotFoundError as e:
        print(e)
        # 모델이 없으면 빈 결과, 0, 0을 반환하여 언패킹 오류 방지
        return [], 0, 0 

    # 원본에서 해당 지역 + 타입 데이터 필터링
    # df는 이 스크립트의 전역 변수로 로드되어 있습니다.
    df_filtered = df[(df['region'] == region) & (df['type'] == type_)].copy()

    # 피해건수 실측 (연도별)
    count_actual = df_filtered.groupby(df_filtered['ds'].dt.year)['count'].sum().reset_index()
    count_actual.columns = ['year', 'count_actual']

    # 피해액 실측 (연도별)
    amount_actual = df_filtered.groupby(df_filtered['ds'].dt.year)['amount'].sum().reset_index()
    amount_actual.columns = ['year', 'amount_actual']

    # 미래 데이터프레임 생성
    # Prophet 모델의 make_future_dataframe은 모델 학습 시 사용된 ds 컬럼을 기반으로 합니다.
    # 여기서는 model_count를 사용했지만, model_amount를 사용해도 동일합니다.
    future = model_count.make_future_dataframe(periods=periods, freq='YS')

    # 피해건수 예측
    forecast_count = model_count.predict(future)
    forecast_count['year'] = forecast_count['ds'].dt.year
    count_pred = forecast_count[['year', 'yhat']].rename(columns={'yhat': 'count_predicted'})

    # 피해액 예측
    forecast_amount = model_amount.predict(future)
    forecast_amount['year'] = forecast_amount['ds'].dt.year
    amount_pred = forecast_amount[['year', 'yhat']].rename(columns={'yhat': 'amount_predicted'})

    # 실측값과 예측값을 병합
    result = count_actual.merge(count_pred, on='year', how='outer') \
                         .merge(amount_actual, on='year', how='outer') \
                         .merge(amount_pred, on='year', how='outer')

    result['region'] = region
    result['type'] = type_
    result = result.fillna(0) # NaN 값은 0으로 채우기

    # 최종 결과 컬럼 순서 조정
    result = result[['year', 'region', 'type',
                     'amount_actual', 'amount_predicted',
                     'count_actual', 'count_predicted']]

    # === 실제 연도 구간의 평가 지표 (R제곱 포함) ===
    # 실측 데이터가 있는 연도만 필터링하여 성능 평가
    actual_years_data = result[result['amount_actual'] > 0].copy() 

    # 실측 데이터가 없는 경우 (예: 해당 지역에 '전기화재' 데이터가 아예 없는 경우)
    if actual_years_data.empty:
        print(f"⚠️ {region} / {type_}: 실측 데이터가 없어 성능 평가를 할 수 없습니다.")
        return [], 0, 0 

    y_true_amount = actual_years_data['amount_actual']
    y_pred_amount = actual_years_data['amount_predicted']
    y_true_count = actual_years_data['count_actual']
    y_pred_count = actual_years_data['count_predicted']

    # 예측값에 음수가 있을 수 있으므로 클리핑 후 지표를 계산하여 실제 사용될 값 기준의 성능을 평가합니다.
    y_pred_amount_clipped = y_pred_amount.clip(lower=0)
    y_pred_count_clipped = y_pred_count.clip(lower=0)

    amount_mae = mean_absolute_error(y_true_amount, y_pred_amount_clipped)
    amount_mse = mean_squared_error(y_true_amount, y_pred_amount_clipped)
    amount_r2 = r2_score(y_true_amount, y_pred_amount_clipped)

    count_mae = mean_absolute_error(y_true_count, y_pred_count_clipped)
    count_mse = mean_squared_error(y_true_count, y_pred_count_clipped)
    count_r2 = r2_score(y_true_count, y_pred_count_clipped)

    print(f"\n=== 예측 성능 평가 ({region} / {type_}) ===")
    print(f"[피해액] MAE: {amount_mae:,.2f}, MSE: {amount_mse:,.2f}, R²: {amount_r2:.4f}")
    print(f"[건수]   MAE: {count_mae:,.2f}, MSE: {count_mse:,.2f}, R²: {count_r2:.4f}")

    # 최종 예측 결과에서 음수 값 0으로 치환
    result['amount_predicted'] = result['amount_predicted'].clip(lower=0)
    result['count_predicted'] = result['count_predicted'].clip(lower=0)

    return result.to_dict(orient='records'), amount_r2, count_r2

# # --- 메인 실행 블록 ---
# if __name__ == "__main__":
#     # 한글 폰트 설정 (윈도우)
#     font_path = "C:/Windows/Fonts/malgun.ttf"
#     if os.path.exists(font_path): # 폰트 파일이 존재하는지 확인
#         font_name = font_manager.FontProperties(fname=font_path).get_name()
#         rc('font', family=font_name)
#     else:
#         print("경고: Malgun Gothic 폰트를 찾을 수 없습니다. 기본 폰트를 사용합니다.")
#         # 다른 한글 폰트 또는 기본 폰트 설정 (Mac/Linux의 경우 다를 수 있음)
#         # 예: rc('font', family='AppleGothic') for Mac
#         # 예: rc('font', family='NanumGothic') for Linux (설치 필요)

#     # 모든 고유 지역 목록 가져오기 (전국 제외)
#     # df는 이 스크립트의 전역 변수로 로드되어 있습니다.
#     all_regions_in_data = df['region'].unique().tolist()
#     regions_to_evaluate = [r for r in all_regions_in_data if r != '전국']
    
#     # 평가 지표를 저장할 리스트
#     all_amount_r2_scores = []
#     all_count_r2_scores = []
    
#     # 예측할 재해 유형 (현재 코드에서는 '전기화재'만 사용)
#     disaster_type = '전기화재'
#     periods = 3 # 예측할 미래 기간 (년)

#     print(f"\n✨ 모든 지역에 대한 {disaster_type} 예측 성능 평가를 시작합니다...")
#     print(f"평가 대상 지역: {', '.join(regions_to_evaluate)}")

#     for region_name in regions_to_evaluate:
#         print(f"\n--- 지역: {region_name} ---")
#         # predict_and_evaluate 함수는 결과 데이터, 피해액 R2, 피해건수 R2를 반환
#         result_data, amount_r2, count_r2 = predict(region_name, disaster_type, periods)
        
#         # 유효한 R2 값을 반환했을 때만 리스트에 추가
#         if amount_r2 is not None and count_r2 is not None:
#             all_amount_r2_scores.append(amount_r2)
#             all_count_r2_scores.append(count_r2)
        
#         # 각 지역별 예측 결과 데이터프레임을 보고 싶다면 이 부분을 활성화
#         # if result_data: # result_data가 비어있지 않은 경우에만 출력
#         #     print(pd.DataFrame(result_data))
        
#         # 개별 지역별 그래프를 보고 싶다면 이 부분을 활성화
#         # if result_data: # result_data가 비어있지 않은 경우에만 그래프 생성
#         #     df_plot = pd.DataFrame(result_data)
#         #     fig, ax1 = plt.subplots(figsize=(12, 6))
#         #     ax1.plot(df_plot['year'], df_plot['amount_actual'], label='피해액 실측', marker='o', color='blue')
#         #     ax1.plot(df_plot['year'], df_plot['amount_predicted'], label='피해액 예측', marker='o', linestyle='--', color='cyan')
#         #     ax1.set_xlabel('년도')
#         #     ax1.set_ylabel('피해액 (원)', color='blue')
#         #     ax1.tick_params(axis='y', labelcolor='blue')
#         #     ax1.grid(True)
#         #     ax2 = ax1.twinx()
#         #     width = 0.3
#         #     years = df_plot['year'].values
#         #     ax2.bar(years - width/2, df_plot['count_actual'], width=width, label='건수 실측', color='orange', alpha=0.7)
#         #     ax2.bar(years + width/2, df_plot['count_predicted'], width=width, label='건수 예측', color='red', alpha=0.7)
#         #     ax2.set_ylabel('피해건수', color='red')
#         #     ax2.tick_params(axis='y', labelcolor='red')
#         #     lines_1, labels_1 = ax1.get_legend_handles_labels()
#         #     lines_2, labels_2 = ax2.get_legend_handles_labels()
#         #     ax1.legend(lines_1 + lines_2, labels_1 + labels_2, loc='upper left')
#         #     plt.title(f"{region_name} {disaster_type} 피해액 및 피해건수 실측 vs 예측 (Prophet)")
#         #     plt.xticks(rotation=45)
#         #     plt.tight_layout()
#         #     plt.show()

#     # --- 최종 R제곱 점수 통계 출력 ---
#     print("\n" + "="*50)
#     print("📈 전체 지역별 R제곱 점수 통계")
#     print("="*50)

#     if all_amount_r2_scores:
#         print("\n[피해액 예측 R² 점수]")
#         print(f"  최저 R²: {min(all_amount_r2_scores):.4f}")
#         print(f"  최고 R²: {max(all_amount_r2_scores):.4f}")
#         print(f"  평균 R²: {sum(all_amount_r2_scores) / len(all_amount_r2_scores):.4f}")
#     else:
#         print("\n피해액 예측 R² 점수를 계산할 수 있는 지역이 없습니다.")

#     if all_count_r2_scores:
#         print("\n[피해건수 예측 R² 점수]")
#         print(f"  최저 R²: {min(all_count_r2_scores):.4f}")
#         print(f"  최고 R²: {max(all_count_r2_scores):.4f}")
#         print(f"  평균 R²: {sum(all_count_r2_scores) / len(all_count_r2_scores):.4f}")
#     else:
#         print("\n피해건수 예측 R² 점수를 계산할 수 있는 지역이 없습니다.")

#     print("\n모든 지역에 대한 평가가 완료되었습니다. 개별 그래프는 주석 처리되어 있습니다.")
