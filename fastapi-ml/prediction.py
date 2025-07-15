import os
import pandas as pd
import joblib
import matplotlib.pyplot as plt
from matplotlib import font_manager, rc
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score

# 원본 데이터
df = pd.read_excel('./data/ElecData.xlsx')
df['ds'] = pd.to_datetime(df['date'].astype(str) + '-01-01')

def load_models(region):
    """
    선택한 지역의 Prophet 모델 로드
    """
    count_model_path = f'./models/prophet_model_count_{region}.pkl'
    amount_model_path = f'./models/prophet_model_amount_{region}.pkl'

    if not os.path.exists(count_model_path) or not os.path.exists(amount_model_path):
        raise FileNotFoundError(f"❌ {region} 모델이 존재하지 않습니다. 모델을 먼저 학습해주세요!")

    model_count = joblib.load(count_model_path)
    model_amount = joblib.load(amount_model_path)

    return model_count, model_amount

def predict(region='서울특별시', type_='전기화재', periods=3):
    # 지역별 Prophet 모델 로드
    model_count, model_amount = load_models(region)

    # 원본에서 해당 지역 + 타입 데이터 필터링
    df_filtered = df[(df['region'] == region) & (df['type'] == type_)].copy()

    # 피해건수 실측 (연도별)
    count_actual = df_filtered.groupby(df_filtered['ds'].dt.year)['count'].sum().reset_index()
    count_actual.columns = ['year', 'count_actual']

    # 피해액 실측 (연도별)
    amount_actual = df_filtered.groupby(df_filtered['ds'].dt.year)['amount'].sum().reset_index()
    amount_actual.columns = ['year', 'amount_actual']

    # 미래 데이터프레임 생성
    future = model_count.make_future_dataframe(periods=periods, freq='YS')

    # 피해건수 예측
    forecast_count = model_count.predict(future)
    forecast_count['year'] = forecast_count['ds'].dt.year
    count_pred = forecast_count[['year', 'yhat']].rename(columns={'yhat': 'count_predicted'})

    # 피해액 예측
    forecast_amount = model_amount.predict(future)
    forecast_amount['year'] = forecast_amount['ds'].dt.year
    amount_pred = forecast_amount[['year', 'yhat']].rename(columns={'yhat': 'amount_predicted'})

    # merge
    result = count_actual.merge(count_pred, on='year', how='outer') \
                         .merge(amount_actual, on='year', how='outer') \
                         .merge(amount_pred, on='year', how='outer')

    result['region'] = region
    result['type'] = type_
    result = result.fillna(0)

    result = result[['year', 'region', 'type',
                     'amount_actual', 'amount_predicted',
                     'count_actual', 'count_predicted']]

    # === 추가: 실제 연도 구간의 평가 지표 ===
    # actual_years_mask = result['amount_actual'] > 0

    # y_true_amount = result.loc[actual_years_mask, 'amount_actual']
    # y_pred_amount = result.loc[actual_years_mask, 'amount_predicted']
    # y_true_count = result.loc[actual_years_mask, 'count_actual']
    # y_pred_count = result.loc[actual_years_mask, 'count_predicted']

    # amount_mae = mean_absolute_error(y_true_amount, y_pred_amount)
    # amount_mse = mean_squared_error(y_true_amount, y_pred_amount)
    # amount_r2 = r2_score(y_true_amount, y_pred_amount)

    # count_mae = mean_absolute_error(y_true_count, y_pred_count)
    # count_mse = mean_squared_error(y_true_count, y_pred_count)
    # count_r2 = r2_score(y_true_count, y_pred_count)

    # print(f"=== 예측 성능 ({region} / {type_}) ===")
    # print(f"[피해액] MAE: {amount_mae:,.2f}, MSE: {amount_mse:,.2f}, R²: {amount_r2:.4f}")
    # print(f"[건수]   MAE: {count_mae:,.2f}, MSE: {count_mse:,.2f}, R²: {count_r2:.4f}")

    # === 추가: 음수 예측값 0으로 치환 ===
    result['amount_predicted'] = result['amount_predicted'].clip(lower=0)
    result['count_predicted'] = result['count_predicted'].clip(lower=0)

    return result.to_dict(orient='records')

# 실행 테스트

# if __name__ == "__main__":
#     # 한글 폰트 설정 (윈도우)
#     font_path = "C:/Windows/Fonts/malgun.ttf"
#     font_name = font_manager.FontProperties(fname=font_path).get_name()
#     rc('font', family=font_name)

#     region = "서울특별시"
#     periods = 3
#     results = predict(region, '전기화재', periods)
#     df_result = pd.DataFrame(results)

#     fig, ax1 = plt.subplots(figsize=(12, 6))

#     # 피해액: 실제 vs 예측 (왼쪽 y축)
#     ax1.plot(df_result['year'], df_result['amount_actual'], label='피해액 실측', marker='o', color='blue')
#     ax1.plot(df_result['year'], df_result['amount_predicted'], label='피해액 예측', marker='o', linestyle='--', color='cyan')
#     ax1.set_xlabel('년도')
#     ax1.set_ylabel('피해액 (원)', color='blue')
#     ax1.tick_params(axis='y', labelcolor='blue')
#     ax1.grid(True)

#     # 피해건수: 실제 vs 예측 (오른쪽 y축)
#     ax2 = ax1.twinx()
#     width = 0.3
#     years = df_result['year'].values
#     ax2.bar(years - width/2, df_result['count_actual'], width=width, label='건수 실측', color='orange', alpha=0.7)
#     ax2.bar(years + width/2, df_result['count_predicted'], width=width, label='건수 예측', color='red', alpha=0.7)
#     ax2.set_ylabel('피해건수', color='red')
#     ax2.tick_params(axis='y', labelcolor='red')

#     # 범례
#     lines_1, labels_1 = ax1.get_legend_handles_labels()
#     lines_2, labels_2 = ax2.get_legend_handles_labels()
#     ax1.legend(lines_1 + lines_2, labels_1 + labels_2, loc='upper left')

#     plt.title(f"{region} 전기화재 피해액 및 피해건수 실측 vs 예측 (Prophet)")
#     plt.xticks(rotation=45)
#     plt.tight_layout()
#     plt.show()