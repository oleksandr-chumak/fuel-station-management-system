import { ManagerStatus } from "./manager-status.enum";

export class Manager {
    constructor(
        public managerId: number,
        public firstName: string,
        public lastName: string,
        public status: ManagerStatus
    ) {}

    terminate() {
        this.status = ManagerStatus.Deactivated;
    }

    get fullName() {
        return this.firstName + " " + this.lastName;
    }
}