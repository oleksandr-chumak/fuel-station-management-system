export enum ManagerEventType {
    MANAGER_CREATED = "MANAGER_CREATED",
    MANAGER_TERMINATED = "MANAGER_TERMINATED"
}

export class ManagerEvent {
    constructor(public managerId: number) {}
}

export class ManagerCreated extends ManagerEvent {
    constructor(managerId: number) {
        super(managerId);
    }
}

export class ManagerTerminated extends ManagerEvent {
    constructor(managerId: number) {
        super(managerId);
    }
}
