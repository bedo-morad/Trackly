import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {ShipmentGrid} from './components/shipment-grid/shipment-grid';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ShipmentGrid],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('Trackly-Web');
}
