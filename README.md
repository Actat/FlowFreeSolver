# FlowFreeSolver

## FlowFree
- [Google Play](https://play.google.com/store/apps/details?id=com.bigduckgames.flow&hl=ja)
- [Microsoft](https://www.microsoft.com/ja-jp/p/flow-free/9wzdncrdqvpj?activetab=pivot%3Aoverviewtab)
- [App Store](https://itunes.apple.com/jp/app/flow-free/id526641427?mt=8)

> Flow Free*は単純ですが、癖になるパズルゲームです。
> 
> 同じ色をパイプでつないで、フローを作り出します。 Flow Freeでは、すべての色をつないで、ボード全体をパイプでうずめてパズルを解いていきます。 ただ、パイプが交差したり、重なったりすると壊れてしまうので注意が必要です。


## 概要
- 趣味で作ったソルバー
- 対応する色の部分を順に押していくことで問題を入力
- SOLVEボタンを押すと回答が表示される
- RESETボタンを押すと初期化される
- PLUS, MINUSボタンを押すと大きさを変えることができる

## 解答するロジック
- BoardクラスのsolveProblem()
    - つながるべきcellを全部つなぐ
    - つながるべきcellが見つからない場合は適当につないでみる
        - この際，適当につなぐ前の盤面を保存しておき，失敗だったら後で読み出す
        - 画面描画を維持するため
    - ここで，適当に繋ぐcellの選択を適切に行うともっと良くなる
        - のびしろですね．
- ```AsyncTask```によって解答はバックグラウンドで作成される
    - ```reDraw```が定期的に実行され、解答状況が表示される

## 情報の置き場
- 盤面の状態はBoardクラスのメンバ変数に保存されている
- Cellクラスは，color（色や終点始点かどうか）とconnection（つながっているのか，つないではいけないのか，どっちでもないか）を持っている
- ```CanvasView```の```private int color[]```には色の情報が```init_CanvasView```で代入されている
    - ```drawBoard```ではこの配列を利用して色を操作している

## 入出力
- ```CanvasView```で表示と入力の受け取りをしている
- 表示のために```drawBoard```は描画範囲の大きさと盤面の情報を受け取っている
- 入力は```private MainActivity mainActivity```を持っており、```onTouchEvent```内でマスの情報に変換してから```mainActivity```に送っている

