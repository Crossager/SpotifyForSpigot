# SpotifyForSpigot

SpotifyForSpigot is a Spigot plugin designed to enhance your Minecraft server with music functionality. It leverages MIDI and JSON files within the plugin folder to provide an immersive musical experience for players. 
Powered by the [Tactical](https://github.com/Crossager/Tactical/) library, SpotifyForSpigot offers features such as pausing, looping, and forwarding songs seamlessly within the game environment.
Note: The json files must be in the Tactical music format

## Features

- **Searches for MIDI and JSON Files**: The plugin scans the plugin folder for MIDI and JSON files to access a wide range of musical content. 
- **Music Control**: Control the playback of music with features like pausing, looping, and forwarding songs.
- **GUI Integration**: Seamless integration with graphical user interfaces for intuitive control.
- **Command Functionality**: Access music control via commands for easy management.
- **BPM Rounding**: Due to notes being played on ticks, the BPM (beats per minute) will be rounded to one of the following values: 300, 150, 100, 75, 60, 50.
- **Compatibility with Online Sequencer**: Songs from [Online Sequencer](https://onlinesequencer.net/) work exceptionally well with the plugin.

## Installation

1. Download the SpotifyForSpigot.jar file.
2. Download [Tactical](https://github.com/Crossager/Tactical/)
3. Place both jar files into the `plugins` folder of your Spigot server.
4. Restart your server.

## Commands

- `/spotify`: Opens the gui where you control your spotify player
- `/spotifyreload`: Reloads songs from the plugin folder

## Usage

1. Place your MIDI and JSON files in the plugin folder.
2. Use the provided commands to control playback.
3. Enjoy a customized musical experience on your Minecraft server!

## License

This project is licensed under the [MIT License](LICENSE). Feel free to modify and distribute it according to the terms of the license.
