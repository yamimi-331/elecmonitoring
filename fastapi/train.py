import os
import pandas as pd
from prophet import Prophet
import joblib

df = pd.read_excel('./data/ElecData.xlsx')

df_fire = df[df['type'] == '전기화재'].copy()

regions = df_fire['region'].unique()

for region in regions:
    df_region = df_fire[df_fire['region'] == region].copy()
    df_region['ds'] = pd.to_datetime(df_region['date'].astype(str) + '-01-01')

    # 피해건수 학습 데이터
    df_count = df_region[['ds', 'count']].rename(columns={'count': 'y'})

    # 피해액 학습 데이터
    df_amount = df_region[['ds', 'amount']].rename(columns={'amount': 'y'})

    # 데이터가 2개 이상인 경우에만 학습 진행
    if df_count['y'].dropna().shape[0] >= 2:
        model_count = Prophet()
        model_count.fit(df_count)
        joblib.dump(model_count, f'./fastapi/models/prophet_model_count_{region}.pkl')
    else:
        print(f"⚠️ {region} 피해건수 데이터가 충분하지 않아 학습하지 않음")

    if df_amount['y'].dropna().shape[0] >= 2:
        model_amount = Prophet()
        model_amount.fit(df_amount)
        joblib.dump(model_amount, f'./fastapi/models/prophet_model_amount_{region}.pkl')
    else:
        print(f"⚠️ {region} 피해액 데이터가 충분하지 않아 학습하지 않음")

    print(f"✅ {region} 모델 학습 완료 (가능한 경우)")
