export const cookieHandler = {
    setCookie(name: string, value: any, existDays: number, path: string) {
        let d = new Date();
        d.setTime(d.getTime() + (existDays * 24 * 60 * 60 * 1000));
        let expires = "expires=" + d.toUTCString();
        document.cookie = name + "=" + value + ";" + expires + ";path=" + path;
    },
    removeCookie(name: string, path: string) {
        cookieHandler.setCookie(name, '', 0, path)
    },
    getCookie(cname: string): string {
        let name = cname + "=";
        let decodedCookie = decodeURIComponent(document.cookie);
        let ca = decodedCookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(name) == 0) {
                return c.substring(name.length, c.length);
            }
        }
        return "";
    }
}