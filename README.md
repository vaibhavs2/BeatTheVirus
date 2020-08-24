# android-Blutetooth
# BeatTheVirus

## Introduction
In This project, we build an android app that will help to follow social distancing during COVID-19. The basic idea behind this app is the estimation of the distance between two mobile devices on the basis of the signal strength of the Bluetooth signal received at the mobile device. As any other device whose bluetooth is on comes close ( less than 2m) to your device, the app will sound an alarm and alert you to maintain a distance. So that you can maintain social distancing to each other. We use the Android Beacon library to calculate the distance.  App will scan nearby devices in every 15sec.


## Hardware Requirements
An Android device
Having Bluetooth capability
Its a concept of finding distance between two person so both sould have android bluetooth enabled devce.
## In future
We can use Bluetooth Low energy that can save battery as bluetooth need to always in scanning mode. As if app is running it takes lot of resources from Mobile phone.
And using caliberation we can enhance precise distance.

## Software Requirements
Bluetooth Discoverable (always)
Android Version 5 or above

## Process Flow
![Image of Yaktocat](https://github.com/vaibhavs2/android-Blutetooth/blob/master/processflow.png)



## Data Flow Diagram
![Image of Yaktocat](https://github.com/vaibhavs2/android-Blutetooth/blob/master/dataflow.png)

## References
[What exactly is txPower for Bluetooth LE and how is it used?](https://stackoverflow.com/questions/36862185/what-exactly-is-txpower-for-bluetooth-le-and-how-is-it-used/36862382)

[Android Beacon Library](https://altbeacon.github.io/android-beacon-library/distance-calculations2.html)

[Understanding ibeacon distancing](https://stackoverflow.com/questions/20416218/understanding-ibeacon-distancing)

[Get Tx Power of BLE Beacon in Android](https://stackoverflow.com/questions/29790853/get-tx-power-of-ble-beacon-in-android)
