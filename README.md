# TDX Analysis

本專案主要以 Kotlin 分析 [運輸資料流通服務 TDX](https://tdx.transportdata.tw/) 平台之部分資料，資料來源皆由此平台提供檔案下載至本地端進行分析，
並遵守 [資料授權利用規範](https://tdx.transportdata.tw/term)。

## 票價檔說明

由於原始檔案 `ODFare.json` 過於巨大 (563 MB) 以致無法讀取與分析，因此我將檔案以 python 將其簡化，得到了 `resource/tdx/TRA/ODFare-Simple.json`，檔案大小約 41.6 MB。

<details>
  <summary>python 程式碼</summary>

```python
import json
from itertools import groupby

od_fares = json.load(open("ODFare.json", encoding="utf-8"))["ODFares"]

# optimize original json
lst = []
for odf in od_fares:
  lst.append({
    "originID": odf["OriginStationID"],
    "destID": odf["DestinationStationID"],
    "trainType": odf["TrainType"],
    "dist": odf["TravelDistance"],
    "price": odf["Fares"][0]["Price"],
  })

# grouping to filter valuable data
result = [min(group, key=lambda x: x["price"]) for group in grouped_data.values()]
grouped_data = {key: list(group) for key, group in groupby(lst, key=lambda x: (x["originID"], x["destID"], x["trainType"]))}

# write to file
with open("ODFare-Simple.json", 'w') as json_file:
  json.dump(result, json_file)
```

</details>

## 檔案錯誤問題

### 北捷
北捷 (TRTC) 之 `StationTimeTable.json` 中將原應為 `DestinationStationID` 錯誤的弄成了 `DestinationStaionID`，因此程式碼中先行以其提供之原始 key:`DestinationStaionID` 進行讀取。
