import { Injectable } from "@angular/core";
import FuelGrade from "./fuel-grade.enum";


@Injectable({ providedIn: "root" })
export class FuelGradeMapper {

    map(grade: unknown): FuelGrade {
        if(typeof grade !== "string") {
            throw new Error("Fuel grade must be a string");
        }

        switch (grade) {
            case "Diesel":
                return FuelGrade.Diesel;
            case "RON_95":
                return FuelGrade.RON_95;
            case "RON_92":
                return FuelGrade.RON_92;
            default:
                throw new Error(`Cannot transform value: ${grade} to FuelGrade enum`);
        }
    }

}