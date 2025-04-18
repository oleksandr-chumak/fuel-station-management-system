
export enum ManagerStatus {
    Active,
    Deactivated
}

export interface Manager {
    id: number;
    firstName: string;
    lastName: string;
    status: ManagerStatus;
}