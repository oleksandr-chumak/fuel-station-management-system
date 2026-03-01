import { ManagerStatus } from "./manager-status.enum";

export class Manager {
    constructor(
        public managerId: number,
        public firstName: string,
        public lastName: string,
        public fullName: string,
        public email: string,
        public status: ManagerStatus
    ) {}

    terminate() {
        this.status = ManagerStatus.Deactivated;
    }

}