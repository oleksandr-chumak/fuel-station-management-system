

export interface GetManagers {
}

export interface GetManagerById {
    managerId: number;
}

export interface CreateManager {
    firstName: string;
    lastName: string;
    email: string;
}