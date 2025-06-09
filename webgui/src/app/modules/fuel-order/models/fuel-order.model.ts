import FuelGrade from '../../common/fuel-grade.enum';

import FuelOrderStatus from './fuel-order-status.enum';

export default class FuelOrder {
  constructor(
        public id: number, 
        public status: FuelOrderStatus, 
        public fuelGrade: FuelGrade, 
        public amount: number, 
        public fuelStationId: number, 
        public createdAt: Date
  ) {}

  get pending() {
    return this.status === FuelOrderStatus.Pending;
  }

  get confirmed() {
    return this.status === FuelOrderStatus.Confirmed;
  }

  get rejected() {
    return this.status === FuelOrderStatus.Rejected;
  }
}