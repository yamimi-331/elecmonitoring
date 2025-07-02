import os
import pandas as pd
import joblib

# 모델 불러오기
model_count = joblib.load('./fastapi/models/prophet_model_count.pkl')
model_amount = joblib.load('./fastapi/models/prophet_model_amount.pkl')

# 원본 데이터
df = pd.read_excel('./data/ElecData.xlsx')
df['ds'] = pd.to_datetime(df['date'].astype(str), format='%Y')

def predict(region='서울특별시', type_='전기화재', periods=3):
    # 필터링
    df_filtered = df[(df['region'] == region) & (df['type'] == type_)].copy()

    # 피해건수 실측
    count_actual = df_filtered.groupby(df_filtered['ds'].dt.year)['count'].sum().reset_index()
    count_actual.columns = ['year', 'count_actual']

    # 피해액 실측
    amount_actual = df_filtered.groupby(df_filtered['ds'].dt.year)['amount'].sum().reset_index()
    amount_actual.columns = ['year', 'amount_actual']

    # 미래 프레임 (연 단위 → Prophet 은 월단위라 넉넉히)
    future = model_count.make_future_dataframe(periods=periods * 12, freq='M')

    forecast_count = model_count.predict(future)
    forecast_count['year'] = forecast_count['ds'].dt.year
    count_pred = forecast_count.groupby('year')['yhat'].sum().reset_index()
    count_pred.columns = ['year', 'count_predicted']

    forecast_amount = model_amount.predict(future)
    forecast_amount['year'] = forecast_amount['ds'].dt.year
    amount_pred = forecast_amount.groupby('year')['yhat'].sum().reset_index()
    amount_pred.columns = ['year', 'amount_predicted']

    # merge
    result = count_actual.merge(count_pred, on='year', how='outer') \
                         .merge(amount_actual, on='year', how='outer') \
                         .merge(amount_pred, on='year', how='outer')

    result['region'] = region
    result['type'] = type_

    # NaN 처리
    result = result.fillna(0)

    # 컬럼 순서
    result = result[['year', 'region', 'type',
                     'amount_actual', 'amount_predicted',
                     'count_actual', 'count_predicted']]

    return result.to_dict(orient='records')

# 실행 테스트
if __name__ == "__main__":
    data = predict(region='서울특별시', type_='전기화재', periods=3)
    for row in data:
        print(row)
