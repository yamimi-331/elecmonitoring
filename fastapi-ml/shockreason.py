import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import json

# df = pd.read_excel('./data/ShockReason.xlsx', index_col=0)
# df = df.replace(',', '', regex=True).astype(int)

# sums = df.sum(axis=1)

# summary_json = sums.to_dict()
# json_output = json.dumps(summary_json, ensure_ascii=False, indent=2)
# print(json_output)

def shock_reason_json(df):
    # 쉼표 제거 및 타입 변환
    df = df.replace(',', '', regex=True).astype(int)
    sums = df.sum(axis=1)

    # JSON용 dict 생성summary_json = sums.to_dict()
    json_data = sums.to_dict()
    return json_data