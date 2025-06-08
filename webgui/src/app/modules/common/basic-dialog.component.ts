
export default class BasicDialog {

  open = false;

  openDialog(): void {
    this.open = true;
  }

  closeDialog(): void {
    this.open = false;
  }

  handleStateChange(state: boolean): void {
    if(state) {
      this.openDialog();
    } else {
      this.closeDialog();
    }
  }

}