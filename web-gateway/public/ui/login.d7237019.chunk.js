(window.webpackJsonp=window.webpackJsonp||[]).push([[19],{1966:function(e,n,r){"use strict";r.r(n);var t,o=r(14),a=r.n(o),i=r(490),u=r(514),s=Object(u.a)(function(){return a.a.createElement(a.a.Fragment,null)}),c=r(10),l=r.n(c),f=r(131),p=r(53),m=r.n(p),d=r(481),b=r(40),y=r(1),g=r(72),v=r(486),h=r(485),w=r(493),S=r(764),x=r.n(S);function O(e){return(O="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e})(e)}function j(e,n,r,o){t||(t="function"==typeof Symbol&&Symbol.for&&Symbol.for("react.element")||60103);var a=e&&e.defaultProps,i=arguments.length-3;if(n||0===i||(n={children:void 0}),n&&a)for(var u in a)void 0===n[u]&&(n[u]=a[u]);else n||(n=a||{});if(1===i)n.children=o;else if(i>1){for(var s=new Array(i),c=0;c<i;c++)s[c]=arguments[c+3];n.children=s}return{$$typeof:t,type:e,key:void 0===r?null:""+r,ref:null,props:n,_owner:null}}function k(e){for(var n=1;n<arguments.length;n++){var r=null!=arguments[n]?arguments[n]:{},t=Object.keys(r);"function"==typeof Object.getOwnPropertySymbols&&(t=t.concat(Object.getOwnPropertySymbols(r).filter(function(e){return Object.getOwnPropertyDescriptor(r,e).enumerable}))),t.forEach(function(n){I(e,n,r[n])})}return e}function P(e,n){for(var r=0;r<n.length;r++){var t=n[r];t.enumerable=t.enumerable||!1,t.configurable=!0,"value"in t&&(t.writable=!0),Object.defineProperty(e,t.key,t)}}function N(e,n){return!n||"object"!==O(n)&&"function"!=typeof n?C(e):n}function C(e){if(void 0===e)throw new ReferenceError("this hasn't been initialised - super() hasn't been called");return e}function I(e,n,r){return n in e?Object.defineProperty(e,n,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[n]=r,e}var E,T=j(d.O,{}),_=j(d.l,{}),D=j(d.I,{htmlFor:"username"},void 0,"Username"),F=j(d.I,{htmlFor:"password"},void 0,"Password"),A=function(e){function n(){var e,r,t;!function(e,n){if(!(e instanceof n))throw new TypeError("Cannot call a class as a function")}(this,n);for(var o=arguments.length,a=new Array(o),i=0;i<o;i++)a[i]=arguments[i];return N(t,(r=t=N(this,(e=n.__proto__||Object.getPrototypeOf(n)).call.apply(e,[this].concat(a))),Object.defineProperty(C(t),"state",{configurable:!0,enumerable:!0,writable:!0,value:{formData:{username:"",password:""},error:{username:"",password:""},loading:!1}}),Object.defineProperty(C(t),"isValid",{configurable:!0,enumerable:!0,writable:!0,value:function(){var e=t.state,n=e.formData,r=e.error,o=n.username,a=n.password;return m.a.some([o,a],w.a.isInputEmpty)&&m.a.each(y.q,function(e){r[e]=w.a.isInputEmpty(n[e])?y.r.mandatoryFields:""}),t.setState({error:r}),m.a.every(r,w.a.isInputEmpty)}}),Object.defineProperty(C(t),"handleSubmit",{configurable:!0,enumerable:!0,writable:!0,value:function(){var e=function(e){return function(){var n=this,r=arguments;return new Promise(function(t,o){var a=e.apply(n,r);function i(e,n){try{var r=a[e](n),i=r.value}catch(e){return void o(e)}r.done?t(i):Promise.resolve(i).then(u,s)}function u(e){i("next",e)}function s(e){i("throw",e)}u()})}}(regeneratorRuntime.mark(function e(n){var r,o,a,i,u,s;return regeneratorRuntime.wrap(function(e){for(;;)switch(e.prev=e.next){case 0:return r=t.context.fetch,o=t.state.formData,n.preventDefault(),e.next=5,t.isValid();case 5:if(e.sent){e.next=10;break}return e.next=9,t.props.openNotifier({openNotify:!0,message:y.r.emptyField,variant:"warning"});case 9:return e.abrupt("return");case 10:return e.next=12,t.setState({loading:!0});case 12:return e.prev=12,o=k({},o,{rememberMe:!1}),e.next=16,r("".concat(y.b.signIn),{method:"POST",body:JSON.stringify(o),headers:{"Content-Type":"application/json"}});case 16:if(200!==(a=e.sent).status){e.next=34;break}return e.next=20,a.json();case 20:return i=e.sent,t.props.login(k({},o,{token:i.resources.token})),e.next=24,r("".concat(y.b.userInfo));case 24:return u=e.sent,e.next=27,u.json();case 27:return s=e.sent,t.props.login(k({},o,{userId:s.resources.keys.key,token:i.resources.token,role:m.a.chain(s.resources.roles).map(function(e){return e.role}).value()})),b.a.push("/home"),e.next=32,t.props.openNotifier({openNotify:!0,message:y.r.loginSuccess,variant:"success"});case 32:e.next=36;break;case 34:return e.next=36,t.props.openNotifier({openNotify:!0,message:y.r.loginFailed,variant:"error"});case 36:return e.next=38,t.setState({loading:!1});case 38:e.next=46;break;case 40:return e.prev=40,e.t0=e.catch(12),e.next=44,t.props.openNotifier({openNotify:!0,message:y.r.loginFailed,variant:"error"});case 44:return e.next=46,t.setState({loading:!1});case 46:case"end":return e.stop()}},e,this,[[12,40]])}));return function(n){return e.apply(this,arguments)}}()}),Object.defineProperty(C(t),"handleChange",{configurable:!0,enumerable:!0,writable:!0,value:function(e){var n=t.state,r=n.formData,o=n.error,a=e&&e.target,i=a.id,u=a.value;r[i]=u,o[i]=w.a.isInputEmpty(r[i])?y.r.mandatoryFields:"",t.setState({formData:r,error:o})}}),r))}return function(e,n){if("function"!=typeof n&&null!==n)throw new TypeError("Super expression must either be null or a function");e.prototype=Object.create(n&&n.prototype,{constructor:{value:e,enumerable:!1,writable:!0,configurable:!0}}),n&&(Object.setPrototypeOf?Object.setPrototypeOf(e,n):e.__proto__=n)}(n,a.a.Component),function(e,n,r){n&&P(e.prototype,n),r&&P(e,r)}(n,[{key:"render",value:function(){var e=this.state,n=e.formData,r=e.loading,t=n.username,o=n.password,a=this.props.classes;return j("main",{className:a.main},void 0,j(d.b,{className:a.avatar,src:x.a},void 0,T),j(d.qb,{style:{color:"#fff",textAlign:"center"},component:"h1",variant:"h5"},void 0,"Syndeia"),_,j(d.T,{className:a.paper},void 0,j("form",{className:a.form},void 0,j(d.y,{margin:"normal",required:!0,fullWidth:!0},void 0,D,j(d.G,{id:"username",name:"username",autoComplete:"username",autoFocus:!0,value:t,onChange:this.handleChange})),j(d.y,{margin:"normal",required:!0,fullWidth:!0},void 0,F,j(d.G,{name:"password",type:"password",id:"password",autoComplete:"current-password",value:o,onChange:this.handleChange})),j(d.d,{type:"submit",fullWidth:!0,variant:"contained",color:"primary",className:a.submit,onClick:this.handleSubmit},void 0,r?j(d.i,{size:22,className:a.progress}):"Sign in"))))}}]),n}();Object.defineProperty(A,"contextTypes",{configurable:!0,enumerable:!0,writable:!0,value:{fetch:l.a.func.isRequired}}),Object.defineProperty(A,"defaultProps",{configurable:!0,enumerable:!0,writable:!0,value:{openNotifier:function(){}}});var R=function(e,n,r,t){E||(E="function"==typeof Symbol&&Symbol.for&&Symbol.for("react.element")||60103);var o=e&&e.defaultProps,a=arguments.length-3;if(n||0===a||(n={children:void 0}),n&&o)for(var i in o)void 0===n[i]&&(n[i]=o[i]);else n||(n=o||{});if(1===a)n.children=t;else if(a>1){for(var u=new Array(a),s=0;s<a;s++)u[s]=arguments[s+3];n.children=u}return{$$typeof:E,type:e,key:void 0===r?null:""+r,ref:null,props:n,_owner:null}}(Object(f.b)(null,{login:g.b,openNotifier:v.c,setUserInfo:g.d})(Object(h.a)(Object(d.ub)(function(e){return{main:I({width:"auto",display:"block",marginLeft:3*e.spacing.unit,marginRight:3*e.spacing.unit,marginTop:10*e.spacing.unit},e.breakpoints.up(400+3*e.spacing.unit*2),{width:400,marginLeft:"auto",marginRight:"auto"}),paper:{marginTop:5*e.spacing.unit,display:"flex",flexDirection:"column",alignItems:"center",padding:"".concat(2*e.spacing.unit,"px ").concat(3*e.spacing.unit,"px ").concat(3*e.spacing.unit,"px")},avatar:{margin:"".concat(3*e.spacing.unit,"px auto"),backgroundColor:"#1b144e",textAlign:"center"},form:{width:"100%",marginTop:e.spacing.unit},submit:{marginTop:3*e.spacing.unit,backgroundColor:"#1b144e",textTransform:"capitalize"},progress:{margin:0,color:"white"}}})(A))),{});var $,q=function(){return R};function J(e,n,r,t){$||($="function"==typeof Symbol&&Symbol.for&&Symbol.for("react.element")||60103);var o=e&&e.defaultProps,a=arguments.length-3;if(n||0===a||(n={children:void 0}),n&&o)for(var i in o)void 0===n[i]&&(n[i]=o[i]);else n||(n=o||{});if(1===a)n.children=t;else if(a>1){for(var u=new Array(a),s=0;s<a;s++)u[s]=arguments[s+3];n.children=u}return{$$typeof:$,type:e,key:void 0===r?null:""+r,ref:null,props:n,_owner:null}}var L="Log In",W=J(i.a,{showSidebar:!1},void 0,J(q,{title:L}),J(s,{}));n.default=function(){return{chunks:["login"],title:L,component:W}}},493:function(e,n,r){"use strict";var t=r(53),o=r.n(t);n.a={isInputEmpty:function(e){return o.a.isString(e)?!o.a.trim(e):o.a.isEmpty(e)},trimObject:function(e){return o.a.each(e,function(n,r){return e[r]=o.a.trim(n)})}}}}]);
//# sourceMappingURL=login.d7237019.chunk.js.map