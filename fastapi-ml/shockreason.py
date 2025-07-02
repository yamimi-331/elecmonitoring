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

def shock_reason_json(df, year: int = 2023):
    # 쉼표 제거 및 타입 변환
    df.columns = [col.replace(',', '') for col in df.columns.astype(str)]
    df = df.replace(',', '', regex=True).astype(int)

    year_col = str(year)
    if year_col not in df.columns:
        raise ValueError(f"{year}년 데이터가 없습니다.")

    # JSON용 dict 생성summary_json = sums.to_dict()
    json_data = df[year_col].to_dict()
    return json_data