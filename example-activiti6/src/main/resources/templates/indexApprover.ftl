<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8"/>
    <title>审核</title>
    <#--<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"/>-->
    <#--<script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js" />-->
    <#--<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>-->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="http://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js" />
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
    <script src="/main.js"></script>
    <style>
        * {
            font-family: "微软雅黑";
            font-size: 15px;
        }

        body {
            margin: 10px 10px 100px 10px;
        }
    </style>
</head>
<body>
<div class="row">
    <div class="col-md-6 col-md-offset-3">
        <div class="panel panel-default">
            <div class="panel-heading">
                <div>
                    <button class="btn btn-danger" onclick="logout()">退出</button>
                </div>
            </div>
            <div class="panel-body">
                <#if (forms?size > 0)>
                    <table class="table table-hover table-condensed">
                        <thead>
                        <tr>
                            <td>请假标题</td>
                            <td>请假内容</td>
                            <td>请假人</td>
                            <td>状态</td>
                            <td>操作</td>
                        </tr>
                        </thead>
                    <#list forms as form>
                        <tr>
                            <td>${form.title}</td>
                            <td>${form.content}</td>
                            <td>${form.applicant}</td>
                            <td>${form.state}</td>
                            <td>
                                <button class="btn btn-default" onclick=approve(${form.id})>
                                    审批通过
                                </button>
                            </td>
                        </tr>
                    </#list>
                    </table>
                <#else>
                    <div>
                        <br/>暂无请假数据
                    </div>
                </#if>
            </div>
        </div>
    </div>
</div>

</body>
</html>