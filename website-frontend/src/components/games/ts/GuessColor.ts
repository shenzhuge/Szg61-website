import {Game, GameObject, TransformParams} from '@eva/eva.js'
import {RendererSystem} from "@eva/plugin-renderer";
import {Graphics, GraphicsSystem} from '@eva/plugin-renderer-graphics'

enum TemplateType {
    Background,
    Red1,
    Yellow2,
    Blue3,
    Green4,
    Sky5,
    Purple6,
    ChessPlaceholder,
}

// 创建棋子模板函数组
class Template {
    // 对外接口函数
    static getGameObject(name: string, type: TemplateType, transform?: TransformParams): GameObject {
        let re = new GameObject(name, transform)
        this.mountGameObject(re, type)
        return re
    }

    static mountGameObject(gameObj: GameObject, type: TemplateType) {
        this.template.get(type)(gameObj)
    }

    // 棋子模板
    private static template = new Map([
        // 创建背景对象
        [TemplateType.Background, function (o: GameObject) {
            let bg = o.addComponent(new Graphics()).graphics;
            bg.beginFill(0xE0FFFF);
            bg.lineStyle(3, 0x7A8B8B, 1, 0);
            bg.drawRect(0, 0, 600, 720);
            bg.endFill();

            // 左侧颜色盒子
            bg.beginFill(0xDEB887);
            bg.lineStyle(3, 0xCD853F, 1, 0); // 边缘绘制在内部
            bg.drawRoundedRect(10, 10, 100, 620, 20); // 颜色盒子
            bg.endFill();
            bg.lineStyle(3, 0xEE9A49, 1, 1); // 边缘绘制在外部
            for (let i = 0; i < 6; i++) {
                bg.drawCircle(60, 70 + i * 100, 40);
            }

            // 右侧棋盘
            bg.beginFill(0xEEEEE0);
            bg.lineStyle(3, 0x8B8B83, 1, 0);
            bg.drawRoundedRect(120, 10, 470, 700, 40);
            bg.endFill();

            // 结果指示灯
            for (let i = 0; i < 6; i++) {
                bg.beginFill(0xC1CDC1);
                bg.lineStyle(0);
                bg.drawRoundedRect(530, 45 + i * 100, 50, 50, 10);
                bg.endFill();
                bg.beginFill(0x8B8B83);
                bg.drawCircle(530 + 13, 45 + i * 100 + 13, 10);
                bg.drawCircle(530 + 37, 45 + i * 100 + 13, 10);
                bg.drawCircle(530 + 13, 45 + i * 100 + 37, 10);
                bg.drawCircle(530 + 37, 45 + i * 100 + 37, 10);
                bg.endFill();
            }

            return bg;
        }],
        // 棋子模板
        [TemplateType.Red1, function (o: GameObject) {
            let temp = o.addComponent(new Graphics()).graphics;
            temp.beginFill(0xFF4500);
            temp.drawCircle(0, 0, 40);
            temp.endFill();
            temp.beginFill(0xFFFFFF);
            temp.drawCircle(0, 0, 14);
            temp.endFill();
        }],
        [TemplateType.Yellow2, function (o: GameObject) {
            let temp = o.addComponent(new Graphics()).graphics;
            temp.beginFill(0xEEEE00);
            temp.drawCircle(0, 0, 40);
            temp.endFill();
            temp.beginFill(0xFFFFFF);
            temp.drawCircle(0, -20, 10);
            temp.drawCircle(0, 20, 10);
            temp.endFill();
        }],
        [TemplateType.Blue3, function (o: GameObject) {
            let temp = o.addComponent(new Graphics()).graphics;
            temp.beginFill(0x1E90FF);
            temp.drawCircle(0, 0, 40);
            temp.endFill();
            temp.beginFill(0xFFFFFF);
            temp.drawCircle(0, -24, 8);
            temp.drawCircle(0, 0, 8);
            temp.drawCircle(0, 24, 8);
            temp.endFill();
        }],
        [TemplateType.Green4, function (o: GameObject) {
            let temp = o.addComponent(new Graphics()).graphics;
            temp.beginFill(0x00FF7F);
            temp.drawCircle(0, 0, 40);
            temp.endFill();
            temp.beginFill(0xFFFFFF);
            temp.drawCircle(15, -15, 8);
            temp.drawCircle(15, 15, 8);
            temp.drawCircle(-15, -15, 8);
            temp.drawCircle(-15, 15, 8);
            temp.endFill();
        }],
        [TemplateType.Sky5, function (o: GameObject) {
            let temp = o.addComponent(new Graphics()).graphics;
            temp.beginFill(0x00F5FF);
            temp.drawCircle(0, 0, 40);
            temp.endFill();
            temp.beginFill(0xFFFFFF);
            temp.drawCircle(20, -20, 8);
            temp.drawCircle(20, 20, 8);
            temp.drawCircle(0, 0, 8);
            temp.drawCircle(-20, -20, 8);
            temp.drawCircle(-20, 20, 8);
            temp.endFill();
        }],
        [TemplateType.Purple6, function (o: GameObject) {
            let temp = o.addComponent(new Graphics()).graphics;
            temp.beginFill(0x9932CC);
            temp.drawCircle(0, 0, 40);
            temp.endFill();
            temp.beginFill(0xFFFFFF);
            temp.drawCircle(-12, -24, 8);
            temp.drawCircle(-12, 0, 8);
            temp.drawCircle(-12, 24, 8);
            temp.drawCircle(12, -24, 8);
            temp.drawCircle(12, 0, 8);
            temp.drawCircle(12, 24, 8);
            temp.endFill();
        }],
        // 棋盘空棋子凹槽模板
        [TemplateType.ChessPlaceholder, function (o: GameObject) {
            let re = o.addComponent(new Graphics()).graphics;
            re.lineStyle(3, 0xB5B5B5, 1, 1);
            re.beginFill(0xCFCFCF);
            re.drawCircle(0, 0, 40);
            re.endFill();
            re.lineStyle(0);
            re.beginFill(0x3CB371);
            re.drawCircle(0, 0, 5);
            re.endFill();
        }]
    ])
}

// 运行参数管理类
export class MainController {
    game: Game

    constructor(canvas: HTMLCanvasElement) {
        this.game = new Game({
            frameRate: 60,
            systems: [
                new RendererSystem({
                    canvas: canvas,
                    width: 600,
                    height: 720,
                }),
                new GraphicsSystem()
            ]
        })
    }

    test() {
        let o = Template.getGameObject('o', TemplateType.Background, {
            position: {x: 0, y: 0}
        })
        this.game.scene.addChild(o)
    }
}