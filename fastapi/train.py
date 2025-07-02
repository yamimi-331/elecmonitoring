import os
import pandas as pd
from prophet import Prophet
import joblib

df = pd.read_excel('./data/ElecData.xlsx')

# 전기화재만 예시로 학습
df_fire = df[df['type'] == '전기화재'].copy()

# ✅ 연도(int or float) → str → datetime
df_fire['ds'] = pd.to_datetime(df_fire['date'].astype(str), format='%Y')

# 피해건수 Prophet
df_count = df_fire[['ds', 'count']].copy()
df_count = df_count.rename(columns={'count': 'y'})

model_count = Prophet()
model_count.fit(df_count)
joblib.dump(model_count, './fastapi/models/prophet_model_count.pkl')

# 피해액 Prophet
df_amount = df_fire[['ds', 'amount']].copy()
df_amount = df_amount.rename(columns={'amount': 'y'})

model_amount = Prophet()
model_amount.fit(df_amount)
joblib.dump(model_amount, './fastapi/models/prophet_model_amount.pkl')

print("✅ 두 Prophet 모델 저장 완료")
