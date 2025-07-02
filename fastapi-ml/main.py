from fastapi import FastAPI, Query
import pandas as pd
from fastapi.responses import JSONResponse
from fastapi.middleware.cors import CORSMiddleware

from prediction import predict
from elecrate import elec_rate_json
from firereason import fire_reason_json
from shockreason import shock_reason_json
from list import get_summary

app = FastAPI()

# CORS 설정 - Spring 서버(8080)와 FastAPI 서버 간 통신
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:8080"],  # Spring 서버 주소
    allow_credentials=True,
    allow_methods=["GET", "POST", "PUT", "DELETE"],
    allow_headers=["*"],
)

@app.get("/predict")
def get_prediction(
    years: int = 3,            # 예측 연도 수
    region: str = '서울특별시',  # 지역명
    type_: str = '전기화재'     # 화재 타입 (기본값)
):
    try:
        result = predict(periods=years, region=region, type_=type_)
        return JSONResponse(content={
            "status": "success",
            "result": result
        })
    except Exception as e:
        return JSONResponse( 
            status_code=500,
            content={
                "status": "error",
                "message": str(e)
            }
        )
    
# 전체 화재 중 전기 화재 비율 json
@app.get("/elec-rate")
def get_elec_rate():
    try:
        df = pd.read_excel('./data/ElecRate.xlsx')
        result = elec_rate_json(df)
        return JSONResponse(content={
            "status": "success",
            "result": result
        })
    except Exception as e:
        return JSONResponse( 
            status_code=500,
            content={
                "status": "error",
                "message": str(e)
            }
        )

# 전기 화재 주요 요인
@app.get("/fire_reason")
def get_fire_reason(year: int = Query(2023, ge=2013, le=2023)):
    try:
        df = pd.read_excel('./data/FireReason.xlsx', index_col=0)
        result = fire_reason_json(df, year)
        return JSONResponse(content={
            "status": "success",
            "result": result
        })
    except Exception as e:
        return JSONResponse( 
            status_code=500,
            content={
                "status": "error",
                "message": str(e)
            }
        )

# 감전 주요 요인
@app.get("/shock_reason")
def get_shock_reason(year: int = Query(2023, ge=2013, le=2023)):
    try:
        df = pd.read_excel('./data/ShockReason.xlsx', index_col=0)
        result = shock_reason_json(df, year)
        return JSONResponse(content={
            "status": "success",
            "result": result
        })
    except Exception as e:
        return JSONResponse( 
            status_code=500,
            content={
                "status": "error",
                "message": str(e)
            }
        )
    
@app.get("/summary")
def get_elec_summary(region: str = Query("전국"), year: int = Query(2023)):
    try:
        df = pd.read_excel('./data/ElecData.xlsx')
        result = get_summary(df, region, year)
        return JSONResponse(content={
            "status": "success",
            "result": result
        })
    except Exception as e:
        return JSONResponse( 
            status_code=500,
            content={
                "status": "error",
                "message": str(e)
            }
        )