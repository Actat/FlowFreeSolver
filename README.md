# FlowFreeSolver

## FlowFree
- [Google Play](https://play.google.com/store/apps/details?id=com.bigduckgames.flow&hl=ja)
- [Microsoft](https://www.microsoft.com/ja-jp/p/flow-free/9wzdncrdqvpj?activetab=pivot%3Aoverviewtab)
- [App Store](https://itunes.apple.com/jp/app/flow-free/id526641427?mt=8)

> Flow Free*は単純ですが、癖になるパズルゲームです。
> 
> 同じ色をパイプでつないで、フローを作り出します。 Flow Freeでは、すべての色をつないで、ボード全体をパイプでうずめてパズルを解いていきます。 ただ、パイプが交差したり、重なったりすると壊れてしまうので注意が必要です。


## 概要
- 現状5x5のみに対応
- 対応する色の部分を順に押していくことで問題を入力
- SOLVEボタンを押すと回答が表示される
- RESETボタンを押すと初期化される

## 解答するロジック
- バックトラッキングを実装
- ```AsyncTask```によって解答は裏で作成される
    - ```reDraw```が定期的に実行され、解答が表示される

### 解決している条件
- すべてのマスが埋まっていること
- 始点や終点が1つの同じ色のマスと隣接していること
- 始点や終点でないマスが同じ色の2つのマスと隣接していること
- 上の2つの条件について隣接する数が多すぎる場合は解と認めないようになっている

## 情報の保持
- 盤面の状態は```int board[SIZE][SIZE]```に保存されている
    - 配列の中身はマスの状態を表す数値
    - ```-1```は空欄を表す
    - ```0```から```15```の16種類の色が定義されている
    - ```0```から```15```の数値はその色になっているマスであることを表す
    - ```0```から```15```に```100```を足した数値はその色の始点、終点であることを表す
- ```CanvasView```の```private int color[]```には色の情報が```init_CanvasView```で代入されている
    - ```drawBoard```ではこの配列を利用して色を操作している

## GUI
- ```CanvasView```で表示と入力の受け取りをしている
- 表示のために```drawBoard```は描画範囲の大きさと盤面の情報（サイズと中身）を受け取っている
- 入力は```private MainActivity mainActivity```を持っており、```onTouchEvent```内でマスの情報に変換してから```mainActivity```に送っている

