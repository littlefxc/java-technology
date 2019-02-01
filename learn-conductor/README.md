# learn-conductor

## 任务定义

POST http://192.168.212.75:8080/api/metadata/taskdefs
Content-Type:application/json

```json
[{
  "name": "computeMoneyAndGoods",
  "retryCount": 3,
  "timeoutSeconds": 1200,
  "timeoutPolicy": "TIME_OUT_WF",
  "retryLogic": "FIXED",
  "retryDelaySeconds": 600,
  "responseTimeoutSeconds": 3600,
  "inputKeys":["buy_num","goods_price","user_money","warehouse_count"],
  "outputKeys": [
  	"next_task","msg","total","remain_user_money","remain_goods_count"
  ]
},
{
  "name": "findGoodsById",
  "retryCount": 3,
  "timeoutSeconds": 1200,
  "timeoutPolicy": "TIME_OUT_WF",
  "retryLogic": "FIXED",
  "retryDelaySeconds": 600,
  "responseTimeoutSeconds": 3600,
  "inputKeys":[],
  "outputKeys": []
},{
  "name": "findWareHouseByGoodsId",
  "retryCount": 3,
  "timeoutSeconds": 1200,
  "timeoutPolicy": "TIME_OUT_WF",
  "retryLogic": "FIXED",
  "retryDelaySeconds": 600,
  "responseTimeoutSeconds": 3600,
  "inputKeys":[],
  "outputKeys": []
},{
  "name": "findUserById",
  "retryCount": 3,
  "timeoutSeconds": 1200,
  "timeoutPolicy": "TIME_OUT_WF",
  "retryLogic": "FIXED",
  "retryDelaySeconds": 600,
  "responseTimeoutSeconds": 3600,
  "inputKeys":[],
  "outputKeys": []
},{
  "name": "not_enough_money_to_pay",
  "retryCount": 3,
  "timeoutSeconds": 1200,
  "timeoutPolicy": "TIME_OUT_WF",
  "retryLogic": "FIXED",
  "retryDelaySeconds": 600,
  "responseTimeoutSeconds": 3600,
  "inputKeys":[],
  "outputKeys": []
},{
  "name": "not_enough_goods",
  "retryCount": 3,
  "timeoutSeconds": 1200,
  "timeoutPolicy": "TIME_OUT_WF",
  "retryLogic": "FIXED",
  "retryDelaySeconds": 600,
  "responseTimeoutSeconds": 3600,
  "inputKeys":[],
  "outputKeys": []
},{
  "name": "updateUserMoneyById",
  "retryCount": 3,
  "timeoutSeconds": 1200,
  "timeoutPolicy": "TIME_OUT_WF",
  "retryLogic": "FIXED",
  "retryDelaySeconds": 600,
  "responseTimeoutSeconds": 3600,
  "inputKeys":[],
  "outputKeys": []
},{
  "name": "updateWareHouseByGoodsId",
  "retryCount": 3,
  "timeoutSeconds": 1200,
  "timeoutPolicy": "TIME_OUT_WF",
  "retryLogic": "FIXED",
  "retryDelaySeconds": 600,
  "responseTimeoutSeconds": 3600,
  "inputKeys":[],
  "outputKeys": []
}]
```

## 流程输入

商品ID：${workflow.input.goods_id}
购买商品数量：${workflow.input.buy_num}
用户ID：${workflow.input.user_id}

## 流程定义

POST http://192.168.212.75:8080/api/metadata/workflow
Content-Type:application/json

```json
{
  "name": "shopping_with_money",
  "description": "购买商品",
  "version": 1,
  "tasks": [
    {
      "name": "fork_join_findGoodsById_findUserById",
      "taskReferenceName": "fork_join_findGoodsById_findUserById",
      "type": "FORK_JOIN",
      "forkTasks": [
        [{
          "name": "findGoodsById",
          "description": "查询商品",
          "taskReferenceName": "findGoodsById",
          "inputParameters": {
            "http_request": {
              "uri": "http://192.168.120.63:8080/api/goods/${workflow.input.goods_id}",
              "method": "GET"
            }
          },
          "type": "HTTP"
        }],
        [{
          "name": "findWareHouseByGoodsId",
          "description": "查询仓库",
          "taskReferenceName": "findWareHouseByGoodsId",
          "inputParameters": {
            "http_request": {
              "uri": "http://192.168.120.63:8080/api/warehouses/search/findByGoodsId?goodsId=${workflow.input.goods_id}",
              "method": "GET"
            }
          },
          "type": "HTTP"
        }],
        [{
          "name": "findUserById",
          "description": "查询用户",
          "taskReferenceName": "findUserById",
          "inputParameters": {
            "http_request": {
              "uri": "http://192.168.120.63:8080/api/users/${workflow.input.user_id}",
              "method": "GET"
            }
          },
          "type": "HTTP"
        }]
      ]
    },
    {
      "name": "join_goods_user_warehouse",
      "taskReferenceName": "join_goods_user_warehouse",
      "type": "JOIN",
      "joinOn": ["findGoodsById","findUserById","findWareHouseByGoodsId"]
    },
    {
      "name": "computeMoneyAndGoods",
      "description": "计算商品总价",
      "taskReferenceName": "computeMoneyAndGoods",
      "inputParameters": {
        "buy_num": "${workflow.input.buy_num}", 
        "goods_price": "${join_goods_user_warehouse.output.findGoodsById.response.body.price}", 
        "user_money": "${join_goods_user_warehouse.output.findUserById.response.body.money}",
        "warehouse_count": "${join_goods_user_warehouse.output.findWareHouseByGoodsId.response.body.count}"
      },
      "type": "SIMPLE"
    },
    {
      "name": "decide_task_pay_or_noPay",
      "taskReferenceName": "decide_task_pay_or_noPay",
      "inputParameters": {
        "case_value_param": "${computeMoneyAndGoods.output.next_task}"
      },
      "type": "DECISION",
      "caseValueParam": "case_value_param",
      "decisionCases": {
        "not_enough_money_to_pay": [
          {
            "name": "not_enough_money_to_pay",
            "description": "没有足够的钱",
            "taskReferenceName": "not_enough_money_to_pay",
            "inputParameters": {
              "http_request": {"uri": "http://192.168.120.63:8080/api/goods/out?msg=${computeMoneyAndGoods.output.msg}","method": "GET"}
            },
            "type": "HTTP"
          }
        ],
        "not_enough_goods": [
          {
            "name": "not_enough_goods",
            "taskReferenceName": "not_enough_goods",
            "inputParameters": {
              "http_request": {"uri": "http://192.168.120.63:8080/api/goods/out?msg=${computeMoneyAndGoods.output.msg}","method": "GET"}
            },
            "type": "HTTP"
          }
        ],
        "pay": [
          {
            "name": "fork_join_updateUserMoneyById_updateWareHouseByGoodsId",
            "taskReferenceName": "fork_join_updateUserMoneyById_updateWareHouseByGoodsId",
            "type": "FORK_JOIN",
            "forkTasks": [
              [{
                "name": "updateUserMoneyById",
                "taskReferenceName": "updateUserMoneyById",
                "inputParameters": {
                  "http_request": {
                    "uri": "http://192.168.120.63:8080/api/users/${workflow.input.user_id}",
                    "method": "PUT",
                    "contentType":"application/json",
                    "body":{
                      "id": "${join_goods_user_warehouse.output.findUserById.response.body.id}",
                      "gmt_create": "${join_goods_user_warehouse.output.findUserById.response.body.gmt_create}",
                      "gmt_modified": "${join_goods_user_warehouse.output.findUserById.response.body.gmt_modified}",
                      "name": "${join_goods_user_warehouse.output.findUserById.response.body.name}",
                      "money":"${computeMoneyAndGoods.output.remain_user_money}"
                    }
                  }
                },
                "type": "HTTP"
              }],
              [{
                "name": "updateWareHouseByGoodsId",
                "taskReferenceName": "updateWareHouseByGoodsId",
                "inputParameters": {
                  "http_request": {
                    "uri": "http://192.168.120.63:8080/api/warehouses/${workflow.input.goods_id}",
                    "method": "PUT",
                    "contentType":"application/json",
                    "body":{
                      "id": "${join_goods_user_warehouse.output.findWareHouseByGoodsId.response.body.id}",
                      "gmt_create": "${join_goods_user_warehouse.output.findWareHouseByGoodsId.response.body.gmt_create}",
                      "gmt_modified": "${join_goods_user_warehouse.output.findWareHouseByGoodsId.response.body.gmt_modified}",
                      "goods_id": "${join_goods_user_warehouse.output.findWareHouseByGoodsId.response.body.goods_id}",
                      "count":"${computeMoneyAndGoods.output.remain_goods_count}"
                    }
                  }
                },
                "type": "HTTP"
              }]
            ]
          },
          {
            "name": "join_user_warehouse",
            "taskReferenceName": "join_user_warehouse",
            "type": "JOIN",
            "joinOn": ["updateUserMoneyById","updateWareHouseByGoodsId"]
          }
        ]
      }
    }
  ],
  "outputParameters": {
    "decide_task_pay_or_noPay.join_user_warehouse.output": "${join_user_warehouse.output}",
    "decide_task_pay_or_noPay.not_enough_money_to_pay": "${not_enough_money_to_pay.output}",
    "decide_task_pay_or_noPay.not_enough_goods": "${not_enough_goods.output}"
  },
  "schemaVersion": 2
}
```

## 流程启动

POST http://192.168.212.75:8080/api/workflow/shopping_with_money
Content-Type:application/json

{
	"goods_id":1,
	"buy_num":1,
	"user_id":1
}