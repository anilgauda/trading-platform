const XHR = {
    token: null,

    createUser: (username, password) => {
        $.ajax("user/register", {
            data: JSON.stringify({username: username, password: password}),
            contentType: 'application/json',
            type: 'POST'
        });
    },

    authenticate: (username, password) => {
        $.ajax("user/authenticate", {
            data: JSON.stringify({username: username, password: password}),
            contentType: 'application/json',
            type: 'POST',
            success: (data) => {
                XHR.token = data.jwttoken;
                sessionStorage.setItem("token", XHR.token);
            }
        });
    },

    get: (url, data, fn) => {
        const reqData = data || {};
        $.ajax
        ({
            type: "GET",
            url: url,
            dataType: 'json',
            headers: {
                "Authorization": 'Bearer ' + XHR.token
            },
            data: data,
            success: fn
        });
    },

    post: (url, data, fn) => {
        const reqData = data || null;
        $.ajax
        ({
            type: "GET",
            url: url,
            dataType: 'json',
            headers: {
                "Authorization": 'Bearer ' + XHR.token
            },
            data: data,
            success: fn
        });
    }

};

if (sessionStorage.getItem("token") != null) {
    XHR.token = sessionStorage.getItem("token");
}