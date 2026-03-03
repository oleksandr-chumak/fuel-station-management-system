export enum ActorType {
    User = "USER",
    System = "SYSTEM"
}

export class Actor {

    constructor(public id: number, public type: ActorType) {}

    system(): boolean {
        return this.type === ActorType.System;
    }

    user(): boolean {
        return this.type === ActorType.User;
    }

}