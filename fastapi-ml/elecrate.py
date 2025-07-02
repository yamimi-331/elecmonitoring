import pandas as pd
import matplotlib.pyplot as plt
import json

df = pd.read_excel('./data/ElecRate.xlsx')
for col in ['Total', 'Elec']:
        df[col] = pd.to_numeric(df[col].astype(str).str.replace(',', ''), errors='coerce')
df['Date'] = df['Date'].astype(str)

# # 영역형 차트 시각화
# plt.rcParams['font.family'] = 'Malgun Gothic'

# plt.figure(figsize=(10, 6))
# plt.fill_between(df['Date'], df['Total'], label='Total', alpha=0.5)
# plt.fill_between(df['Date'], df['Elec'], label='Elec', alpha=0.7)

# plt.title('년도별 화재 전체와 전기 화재 비율')
# plt.xlabel('Year')
# plt.legend()
# plt.grid(True)
# plt.show()

# JSON 만들기
json_data = {year: [total, elec] for year, total, elec in zip(df['Date'], df['Total'], df['Elec'])}
print(json.dumps(json_data, ensure_ascii=False, indent=2))


def elec_rate_json(df):
    # 쉼표 제거 및 타입 변환 (Total, Elec)
    for col in ['Total', 'Elec']:
        df[col] = pd.to_numeric(df[col].astype(str).str.replace(',', ''), errors='coerce')
    df['Date'] = df['Date'].astype(str)

    # JSON용 dict 생성
    json_data = {year: [total, elec] for year, total, elec in zip(df['Date'], df['Total'], df['Elec'])}
    return json_data