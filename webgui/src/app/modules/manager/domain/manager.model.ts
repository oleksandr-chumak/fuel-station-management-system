import ManagerStatus from "./manager-status.enum";

export default class Manager {
    constructor(
        public id: number,
        public firstName: string,
        public lastName: string,
        public status: ManagerStatus
    ) {}

    terminate() {
        this.status = ManagerStatus.Deactivated;
    }
}