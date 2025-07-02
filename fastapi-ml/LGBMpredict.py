import joblib
import numpy as np
import pandas as pd
import os
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score
import matplotlib.pyplot as plt
from matplotlib import font_manager, rc

df = pd.read_excel('./data/ElecData.xlsx')
df = df.fillna(0)
df['date'] = df['date'].astype(int)
df = df.drop_duplicates(['region', 'type', 'date'])

def predict(region='서울특별시', type_='전기화재', periods=3):
    # 모델 및 인코더 로드
    model = joblib.load('./models/lgbm_fire_multioutput_model.pkl')
    le_region = joblib.load('./models/lgbm_le_region.pkl')

    # type, region별 데이터 필터링
    df_filtered = df[(df['region'] == region) & (df['type'] == type_)].copy()
    if df_filtered.empty:
        raise ValueError(f"{region} / {type_} 데이터가 없습니다.")

    # 연도별 집계
    count_actual = df_filtered.groupby('date')['count'].sum().reset_index()
    count_actual.columns = ['year', 'count_actual']
    amount_actual = df_filtered.groupby('date')['amount'].sum().reset_index()
    amount_actual.columns = ['year', 'amount_actual']

    # region 인코딩
    if region not in le_region.classes_:
        raise ValueError(f"등록되지 않은 지역명입니다: {region}")
    region_encoded = le_region.transform([region])[0]

    # 실제 데이터가 있는 연도 범위
    years_actual = sorted(df_filtered['date'].unique())
    min_year = df['date'].min()
    last_year = max(years_actual)
    future_years = [last_year + i for i in range(1, periods + 1)]

    # 실제 연도별 예측값 (실제값+예측값 모두)
    X_actual = pd.DataFrame({
        'date': years_actual,
        'region_encoded': [region_encoded] * len(years_actual)
    })
    y_pred_actual = model.predict(X_actual)
    amount_pred_actual = np.expm1(y_pred_actual[:, 0])  # 로그 복원
    count_pred_actual = y_pred_actual[:, 1]
    df_pred_actual = pd.DataFrame({
        'year': years_actual,
        'amount_predicted': amount_pred_actual,
        'count_predicted': count_pred_actual
    })

    # 미래 연도 예측값
    if periods > 0:
        X_future = pd.DataFrame({
            'date': future_years,
            'region_encoded': [region_encoded] * len(future_years)
        })
        y_pred_future = model.predict(X_future)
        amount_pred_future = np.expm1(y_pred_future[:, 0])
        count_pred_future = y_pred_future[:, 1]
        df_pred_future = pd.DataFrame({
            'year': future_years,
            'amount_predicted': amount_pred_future,
            'count_predicted': count_pred_future,
            'amount_actual': 0.0,
            'count_actual': 0.0
        })
    else:
        df_pred_future = pd.DataFrame(columns=['year', 'amount_predicted', 'count_predicted', 'amount_actual', 'count_actual'])

    # merge 실제 연도: 실제값+예측값
    df_result = df_pred_actual.merge(amount_actual, on='year', how='left') \
                               .merge(count_actual, on='year', how='left')
    df_result['region'] = region
    df_result['type'] = type_
    df_result = df_result.fillna(0)
    df_result = df_result[['year', 'region', 'type',
                           'amount_actual', 'amount_predicted',
                           'count_actual', 'count_predicted']]

    # 미래 연도: 예측값만
    if not df_pred_future.empty:
        df_pred_future['region'] = region
        df_pred_future['type'] = type_
        df_pred_future = df_pred_future[['year', 'region', 'type',
                                         'amount_actual', 'amount_predicted',
                                         'count_actual', 'count_predicted']]
        df_result = pd.concat([df_result, df_pred_future], ignore_index=True)
  # === 추가: 실제 연도 구간의 평가 지표 ===
    actual_years_mask = df_result['amount_actual'] > 0

    y_true_amount = df_result.loc[actual_years_mask, 'amount_actual']
    y_pred_amount = df_result.loc[actual_years_mask, 'amount_predicted']
    y_true_count = df_result.loc[actual_years_mask, 'count_actual']
    y_pred_count = df_result.loc[actual_years_mask, 'count_predicted']

    amount_mae = mean_absolute_error(y_true_amount, y_pred_amount)
    amount_mse = mean_squared_error(y_true_amount, y_pred_amount)
    amount_r2 = r2_score(y_true_amount, y_pred_amount)

    count_mae = mean_absolute_error(y_true_count, y_pred_count)
    count_mse = mean_squared_error(y_true_count, y_pred_count)
    count_r2 = r2_score(y_true_count, y_pred_count)

    print(f"=== 예측 성능 ({region} / {type_}) ===")
    print(f"[피해액] MAE: {amount_mae:,.2f}, MSE: {amount_mse:,.2f}, R²: {amount_r2:.4f}")
    print(f"[건수]   MAE: {count_mae:,.2f}, MSE: {count_mse:,.2f}, R²: {count_r2:.4f}")

    return df_result.to_dict(orient='records')



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

#     plt.title(f"{region} 전기화재 피해액 및 피해건수 실측 vs 예측 (LGBM)")
#     plt.xticks(rotation=45)
#     plt.tight_layout()
#     plt.show()