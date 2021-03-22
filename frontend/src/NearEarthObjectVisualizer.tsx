import { useState, useRef } from "react"
import { NearEarthObjectDisplay } from "./NearEarthObjectDisplay"
import { NearEarthObject, OperationMode } from "./Types"
import axios from 'axios';
import InputAlert from "./InputAlert";

export default function NearEarthObjectVisualizer({mode}: {mode: OperationMode}) {

    const emptyNearEarthObject: NearEarthObject = {
        id: "Loading...",
        name: "Loading...",
        missDistanceKm: 0.0,
        closeApproachDate: "Loading...",
        closeAppraochFullDate: "Loading...",
        orbiting_body: "Loading...",
        relativeVelocityKmPerSec: 0.0,
        estimatedDiameterMeanKm: 0.0,
        nasaJplUrl: "Loading..."
    }

    let [fromDate, setFromDate] = useState("<enter from date>")
    let [toDate, setToDate] = useState("<enter to date>")
    let [nearEarthObj, setNearEearthObject] = useState(emptyNearEarthObject)


    function getOperation(mode: OperationMode) {
        let operation = ""

        switch(mode) {
            case "FIND_CLOSEST":
                operation = "closest"
                break
            case "FIND_LARGEST":
                operation = "largest"
                break
        }

        return operation
    }

    function fetchNearEarthObjectFromBackend(event) {

        setNearEearthObject(emptyNearEarthObject)

        axios.get(`http://localhost:8000/NasaNearEarthObjects/${getOperation(mode)}?from=${fromDate}&to=${toDate}`)
        .then(res => {
          const nearEarthObject = res.data;
          setNearEearthObject(nearEarthObject)
        })
        .catch( e => console.log(e))
    }

    function handleFromDateChange(event) {
        setFromDate(event.target.value)
    }

    function handleToDateChange(event) {
        setToDate(event.target.value)
    }

    return (
        <div className="NearEarthObjectVisualizer">
            <DisplayHeader mode={mode} />
            <h3> Specify time interval:</h3>
            <div>
                <p>FromDate: <input onChange={handleFromDateChange} type="text" value={fromDate}/></p>
                <p>ToDate: <input onChange={handleToDateChange} type="text" value={toDate}/></p>
            </div>
            <button onClick={fetchNearEarthObjectFromBackend}>Find {getOperation(mode)} Near Earth Object</button>
            <h3>Near Earth Object from {fromDate} to {toDate}</h3>
            <NearEarthObjectDisplay nearEarthObject={nearEarthObj} />
            <InputAlert fromInput={fromDate} toInput={toDate} />
        </div>
    )
}

function DisplayHeader({mode}: {mode: OperationMode}) {
    switch(mode) {
        case "FIND_CLOSEST":
            return <h2>Closest Near Earth Object in time span</h2>
        case "FIND_LARGEST":
            return <h2>Largest Near Earth Object in time span</h2>
    }
}