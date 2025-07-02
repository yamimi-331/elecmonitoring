import pandas as pd
import numpy as np
import joblib
from lightgbm import LGBMRegressor
from sklearn.model_selection import train_test_split
from sklearn.multioutput import MultiOutputRegressor
from sklearn.preprocessing import LabelEncoder

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
# 피해액은 로그 변환(0 방지 위해 1 더함)
y = df[['amount', 'count']].copy()
y['amount'] = np.log1p(y['amount'])

# 학습/검증 분리
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# MultiOutputRegressor에 LGBMRegressor 사용
model = MultiOutputRegressor(LGBMRegressor(n_estimators=100, random_state=42))
model.fit(X_train, y_train)

# 예측 테스트
_ = model.predict(X_test)

# 모델, 인코더 저장
joblib.dump(model, './models/lgbm_fire_multioutput_model.pkl')
joblib.dump(le_region, './models/lgbm_le_region.pkl')

print('LightGBM 학습 완료') 