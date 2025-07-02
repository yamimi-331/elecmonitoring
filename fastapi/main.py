# fastapi/main.py

from fastapi import FastAPI
from prediction import predict_future
import pandas as pd
from fastapi.responses import JSONResponse
from elecrate import elec_rate_json
from firereason import fire_reason_json

app = FastAPI()

@app.get("/predict")
def get_prediction(months: int = 12):
    result = predict_future(periods=months)
    # pandas DataFrame → dict
    return result.to_dict(orient="records")

# 전체 화재 중 전기 화재 비율 json
@app.get("/elec-rate")
def get_elec_rate():
    try:
        df = pd.read_excel('./data/ElecRate.xlsx')
        result = elec_rate_json(df)
        return JSONResponse(content=result)
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)})

# 전기 화재 주요 요인
@app.get("/fire_reason")
def get_fire_reason():
    try:
        df = pd.read_excel('./data/FireReason.xlsx', index_col=0)
        result = fire_reason_json(df)
        return JSONResponse(content=result)
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)})
