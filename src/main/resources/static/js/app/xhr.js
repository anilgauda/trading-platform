const XHR = {
    token: null,

    createUser: (username, password, name, fn) => {
        let displayName = name || "";
        let func = fn || function () {
        };
        $.ajax("/user/register", {
            data: JSON.stringify({username: username, password: password, displayName: displayName}),
            contentType: 'application/json',
            type: 'POST',
            success: func
        });
    },

    authenticate: (username, password, fn, fnErr) => {
        $.ajax("/user/authenticate", {
            data: JSON.stringify({username: username, password: password}),
            contentType: 'application/json',
            type: 'POST',
            success: (response) => {
                if (!response.hasError) {
                    XHR.token = response.data.jwttoken;
                    sessionStorage.setItem("token", XHR.token);
                }
                let func = fn || function () {
                };
                func(response);
            },
            error: fnErr
        });
    },

    get: (url, data, fn, fnErr) => {
        const reqData = data || {};
        $.ajax
        ({
            type: "GET",
            url: url,
            dataType: 'json',
            headers: {
                "Authorization": 'Bearer ' + XHR.token
            },
            data: reqData,
            success: fn,
            error: fnErr
        });
    },

    post: (url, data, fn, fnErr) => {
        const reqData = data || null;
        $.ajax
        ({
            type: "POST",
            url: url,
            dataType: 'json',
            headers: {
                "Authorization": 'Bearer ' + XHR.token
            },
            data: reqData,
            success: fn,
            error: fnErr
        });
    }

};

if (sessionStorage.getItem("token") != null) {
    XHR.token = sessionStorage.getItem("token");
}