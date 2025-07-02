import pandas as pd
import json

def get_summary(df, region: str = "전국", year: int = 2023):
    
    df = df[df['region'] == region]
    df = df[df['date'].astype(int) == year]

    fire = df[df['type'] == '전기화재']
    shock = df[df['type'] == '감전사고']
    
    data = {
        "region": region,
        "year": year,
        "fire_count": int(fire['count'].sum()),
        "fire_amount": int(fire['amount'].sum()),
        "fire_injury": int(fire['injury'].sum()),
        "fire_death": int(fire['death'].sum()),
        "shock_injury": int(shock['injury'].sum()),
        "shock_death": int(shock['death'].sum())
    }

    return data