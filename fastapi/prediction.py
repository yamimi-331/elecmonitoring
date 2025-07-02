# fastapi/prediction.py

import os
import pandas as pd
from prophet import Prophet
import joblib

BASE_DIR = os.path.dirname(os.path.abspath(__file__))

# 저장된 모델 불러오기
model = joblib.load(os.path.join(BASE_DIR, 'model.pkl'))

# 원본 데이터 (Prophet 학습할 때 썼던!)
df = pd.read_excel(os.path.join(BASE_DIR, '../data/ElecData.xlsx'))
df['ds'] = pd.to_datetime(df['date'])
df['y'] = df['count']
df = df[['ds', 'y']]

def predict_future(periods=12, region="서울"):
    # 미래 프레임 생성
    future = model.make_future_dataframe(periods=periods, freq='M')
    forecast = model.predict(future)

    # 실측 데이터와 merge
    merged = pd.merge(forecast, df, on='ds', how='left')

    # 연도만 추출
    merged['year'] = merged['ds'].dt.year

    # 지역 추가
    merged['region'] = region

    # 필요한 컬럼만
    result = merged[['year', 'region', 'y', 'yhat']].tail(periods)

    # 컬럼명 보기 좋게
    result = result.rename(columns={'y': 'actual', 'yhat': 'predicted'})

    # NaN 실측값은 0으로 (미래는 실측 없으니까)
    result['actual'] = result['actual'].fillna(0)

    return result.to_dict(orient="records")

# 테스트용 실행
if __name__ == "__main__":
    result = predict_future(periods=12, region="서울")
    for row in result:
        print(row)
