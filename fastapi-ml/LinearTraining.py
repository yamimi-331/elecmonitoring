import pandas as pd
import numpy as np
import joblib
from sklearn.linear_model import LinearRegression
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split
 
# 데이터 불러오기
df = pd.read_excel('./data/ElecData.xlsx')
df = df.fillna(0)
df['date'] = df['date'].astype(int)
df = df.drop_duplicates(['region', 'type', 'date'])

# 범주형 변수 인코딩 (region)
le_region = LabelEncoder()
df['region_encoded'] = le_region.fit_transform(df['region'])

# 입력 변수: date, region_encoded
X = df[['date', 'region_encoded']]
# 타깃 변수: amount, count
y = df[['amount', 'count']].copy()
y['amount'] = np.log1p(y['amount'])  # 피해액 로그 변환

# 학습/검증 분리 (여기선 전체 데이터로 학습해도 무방)
model = LinearRegression()
model.fit(X, y)

# 모델, 인코더 저장
joblib.dump(model, './models/linear_fire_multioutput_model.pkl')
joblib.dump(le_region, './models/linear_le_region.pkl')

print('선형회귀 학습 완료')
