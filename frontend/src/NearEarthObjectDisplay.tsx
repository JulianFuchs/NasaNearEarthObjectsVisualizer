import { NearEarthObject } from "./Types";

export function NearEarthObjectDisplay({nearEarthObject}: {nearEarthObject: NearEarthObject}) {
    return (
        <div className="NearEarthObjectDisplay">
            <p>Id: {nearEarthObject.id}</p>
            <p>Name: {nearEarthObject.name}</p>
            <p>missDistanceKm: {nearEarthObject.missDistanceKm}</p>
            <p>closeApproachDate: {nearEarthObject.closeApproachDate}</p>
            <p>closeAppraochFullDate: {nearEarthObject.closeAppraochFullDate}</p>
            <p>orbiting_body: {nearEarthObject.orbiting_body}</p>
            <p>relativeVelocityKmPerSec: {nearEarthObject.relativeVelocityKmPerSec}</p>
            <p>estimatedDiameterMeanKm: {nearEarthObject.estimatedDiameterMeanKm}</p>
            <p>nasaJplUrl: {nearEarthObject.nasaJplUrl}</p>
        </div>
    )
}