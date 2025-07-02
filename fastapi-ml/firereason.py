import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import json

df = pd.read_excel('./data/FireReason.xlsx', index_col=0)
df = df.replace(',', '', regex=True).astype(int)

# 최근 5년치만 추출, 원인 별 합계
recent_5yrs = df.iloc[:, -5:]
sums = recent_5yrs.sum(axis=1)

# # 도넛형 차트 시각화
# plt.rcParams['font.family'] = 'Malgun Gothic'

# plt.figure(figsize=(8, 8))
# colors = sns.color_palette('tab10', n_colors=len(sums))

# plt.pie(
#     sums,
#     labels=sums.index,         # 각 원인 이름이 라벨로 뜸
#     startangle=90,
#     colors=colors
# )

# plt.title('최근 5년 단락 원인 비율 (2019~2023)', fontsize=14)
# plt.axis('equal')  # 원형 유지
# plt.tight_layout()
# plt.show()

# JSON 형식으로 딕셔너리 생성
summary_json = sums.to_dict()
json_output = json.dumps(summary_json, ensure_ascii=False, indent=2)
print(json_output)

def fire_reason_json(df):
    # 쉼표 제거 및 타입 변환
    df = df.replace(',', '', regex=True).astype(int)

    recent_5yrs = df.iloc[:, -5:]
    sums = recent_5yrs.sum(axis=1)

    # JSON용 dict 생성summary_json = sums.to_dict()
    json_data = sums.to_dict()
    return json_data