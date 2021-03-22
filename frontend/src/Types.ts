export type NearEarthObject = {
    id: string,
    name: string,
    missDistanceKm: number,
    closeApproachDate: string,
    closeAppraochFullDate: string,
    orbiting_body: string,
    relativeVelocityKmPerSec: number,
    estimatedDiameterMeanKm: number,
    nasaJplUrl: string
  }

  export type OperationMode = "FIND_CLOSEST" | "FIND_LARGEST"