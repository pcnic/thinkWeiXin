var ajax = new Object();
ajax.Submit = function (url, sendData, successEvt, errorEvt)
{
    $.ajax({
        type: "post",
        url: url,
        datatype: "json",
        data: sendData,
        success: function (data)
        {
            if (data && data == 'true')
            {
                successEvt();
            }
            else
            {
                errorEvt();
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown)
        {
            // alert(errorThrown);
            errorEvt();
        }
    });
}
ajax.Submit2 = function (url, sendData, successEvt, errorEvt)
{
    $.ajax({
        type: "post",
        url: url,
        datatype: "json",
        data: sendData,
        async: false,
        success: function (data)
        {
            var flag = data.substring(0, 1);
            var info = "";
            if (data.length > 1) info = data.substring(1);
            successEvt(flag, info);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown)
        {
           //  alert(errorThrown);
            errorEvt();
        }
    });
}
ajax.Submit3 = function (url, sendData, successEvt, errorEvt)
{
    $.ajax({
        type: "post",
        url: url,
        datatype: "json",
        data: sendData,
        async: true,
        success: function (data)
        {
            successEvt(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown)
        {
            // alert(errorThrown);
            errorEvt();
        }
    });
}

