# fastapi/main.py

from fastapi import FastAPI
from prediction import predict_future

app = FastAPI()

@app.get("/predict")
def get_prediction(months: int = 12):
    result = predict_future(periods=months)
    # pandas DataFrame → dict
    return result.to_dict(orient="records")
