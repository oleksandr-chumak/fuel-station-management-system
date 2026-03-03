import { Actor } from "./actor";

export class DomainEvent {
    constructor(public occurredAt: string, public performedBy: Actor) {}
}