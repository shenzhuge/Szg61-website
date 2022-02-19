import {ElMessage} from "element-plus";

export default class FastElMessage {
    public static getController(duration?: number): FastElMessageCtl {
        return new FastElMessageCtl(duration)
    }

    public static info(message: string) {
        this.show('info', message)
    }

    public static success(message: string) {
        this.show('success', message)
    }

    public static error(message: string) {
        this.show('error', message)
    }

    public static warning(message: string) {
        this.show('warning', message)
    }

    private static show(type: "info" | "success" | "warning" | "error", message: string) {
        ElMessage({
            type: type,
            message: message,
        })
    }
}

class FastElMessageCtl {
    private duration: number

    public setDuration(duration: number): FastElMessageCtl {
        this.duration = duration
        return this
    }

    constructor(duration?: number) {
        if (duration) this.duration = duration
        else this.duration = 3000
    }

    public info(message: string) {
        this.show('info', message)
    }

    public success(message: string) {
        this.show('success', message)
    }

    public error(message: string) {
        this.show('error', message)
    }

    public warning(message: string) {
        this.show('warning', message)
    }

    private show(type: "info" | "success" | "warning" | "error", message: string) {
        ElMessage({
            type: type,
            message: message,
            duration: this.duration,
        })
    }
}