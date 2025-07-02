import os
import pandas as pd
import joblib
import matplotlib.pyplot as plt
from matplotlib import font_manager, rc

# 원본 데이터
df = pd.read_excel('./data/ElecData.xlsx')
df['ds'] = pd.to_datetime(df['date'].astype(str) + '-01-01')

def load_models(region):
    """
    선택한 지역의 Prophet 모델 로드
    """
    count_model_path = f'./fastapi/models/prophet_model_count_{region}.pkl'
    amount_model_path = f'./fastapi/models/prophet_model_amount_{region}.pkl'

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

    return result.to_dict(orient='records')

# # 실행 테스트
# if __name__ == "__main__":
#     # 한글 폰트 설정 (윈도우 기준)
#     font_path = "C:/Windows/Fonts/malgun.ttf"
#     font_name = font_manager.FontProperties(fname=font_path).get_name()
#     rc('font', family=font_name)

#     # ✅ 지역 선택
#     region = '서울특별시'
#     type_ = '전기화재'

#     data = predict(region=region, type_=type_, periods=3)
#     df_result = pd.DataFrame(data)

#     # 피해액 그래프
#     plt.figure(figsize=(12, 6))
#     plt.plot(df_result['year'], df_result['amount_actual'], label='피해액 실측', marker='o', color='blue')
#     plt.plot(df_result['year'], df_result['amount_predicted'], label='피해액 예측', marker='o', linestyle='--', color='cyan')
#     plt.title(f'{region} 피해액 실측 vs 예측')
#     plt.xlabel('년도')
#     plt.ylabel('피해액 (원)')
#     plt.legend()
#     plt.grid(True)
#     plt.xticks(rotation=45)
#     plt.tight_layout()
#     plt.show()

#     # 피해건수 그래프
#     plt.figure(figsize=(12, 6))
#     width = 0.35
#     plt.bar(df_result['year'] - width/2, df_result['count_actual'], width=width, label='건수 실측', color='orange', alpha=0.7)
#     plt.bar(df_result['year'] + width/2, df_result['count_predicted'], width=width, label='건수 예측', color='red', alpha=0.7)
#     plt.title(f'{region} 피해건수 실측 vs 예측')
#     plt.xlabel('년도')
#     plt.ylabel('피해건수')
#     plt.legend()
#     plt.grid(True)
#     plt.xticks(rotation=45)
#     plt.tight_layout()
#     plt.show()
