# fastapi/train.py

import pandas as pd
from prophet import Prophet
import joblib  # 모델 저장/불러오기

# 데이터 읽기
df = pd.read_excel('./data/ElecData.xlsx')

# 컬럼명 맞추기
# 예: 사고일시 → ds, 피해건수 → y
df['ds'] = pd.to_datetime(df['date'])
df['y'] = df['count']
df = df[['ds', 'y']]

# Prophet 학습
model = Prophet()
model.fit(df)

# 모델 저장 (pkl)
joblib.dump(model, './fastapi/model.pkl')

print("✅ Prophet 모델 저장 완료: model.pkl")
