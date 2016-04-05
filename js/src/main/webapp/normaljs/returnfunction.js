//返回函数的JavaScript函数

var module1 = {
        objone: {
            get:function(){
                return "objone";
            }
        },

        objtwo: {
            get:function(){
                return "objone";
            }
        }
};

/*第二种写法*/
function getParam(name){
    return name;
}

var module2 = {
    objone: {
        get:getParam("objone")
    },

    objtwo: {
        get:getParam("objtwo")
    }
};