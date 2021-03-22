import React, { ChangeEvent, useState } from 'react';
import logo from './logo.svg';
import { isPropertySignature } from 'typescript';
import { NearEarthObject, OperationMode } from './Types';
import NearEarthObjectVisualizer from './NearEarthObjectVisualizer';

function App() {
  return (
    <div className="Nasa">
      <h1>Nasa Near Earth Objects visualizer</h1>
      <div className="Wrap">
        <NearEarthObjectVisualizer mode={"FIND_CLOSEST"}/>
        <NearEarthObjectVisualizer mode={"FIND_LARGEST"}/>
      </div>
    </div>
  );
}

export default App;