import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import train_test_split
from sklearn.multioutput import MultiOutputRegressor
from sklearn.metrics import mean_absolute_error
import joblib
import os

# 경고 제거
import warnings
warnings.filterwarnings('ignore')

# 데이터 로드 및 전처리
df = pd.read_excel('./data/ElecData.xlsx')
df = df.fillna(0)
df['date'] = df['date'].astype(int)
df = df.drop_duplicates(['region', 'type', 'date'])

# 전기화재만 사용
df = df[df['type'] == '전기화재'].copy()

# 연도 기준 정렬 및 시작 연도 설정
df['year_from_start'] = df['date'] - df['date'].min()

# 저장 폴더
save_dir = './models/by_region'
os.makedirs(save_dir, exist_ok=True)

# 지역별 학습
regions = df['region'].unique()
print(f"총 지역 수: {len(regions)}")

for region in regions:
    df_r = df[df['region'] == region].copy()
    if df_r.shape[0] < 5:
        print(f"⚠ 데이터 부족: {region} (행 {df_r.shape[0]}) - 스킵")
        continue

    X = df_r[['year_from_start']]
    y = df_r[['amount', 'count']]
    y['amount_log'] = np.log1p(y['amount'])
    y_target = y[['amount_log', 'count']]

    X_train, X_test, y_train, y_test = train_test_split(
        X, y_target, test_size=0.2, random_state=42
    )

    model = MultiOutputRegressor(RandomForestRegressor(n_estimators=100, random_state=42))
    model.fit(X_train, y_train)

    y_pred = model.predict(X_test)
    mae_amount = mean_absolute_error(y_test['amount_log'], y_pred[:, 0])
    mae_count = mean_absolute_error(y_test['count'], y_pred[:, 1])

    print(f"[{region}] 피해액(log) MAE: {mae_amount:.4f}, Count MAE: {mae_count:.2f}")

    # 모델 저장 (지역명 안전하게 파일명에 사용)
    safe_region = region.replace('/', '_').replace(' ', '_')
    joblib.dump(model, f'{save_dir}/rf_model_{safe_region}.pkl')

print("✅ 모든 지역별 학습 및 저장 완료")
