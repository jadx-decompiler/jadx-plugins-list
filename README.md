
 ## JADX plugins list

Collection of jadx plugins created by community (aka 'marketplace').

This list holds only plugin locations and does not store artifacts.

Plugin list can be viewed here:
- https://github.com/stars/skylot/lists/jadx-plugins
- in jadx-cli
  ```bash
  jadx plugins --available
  ```
- in jadx-gui
  ```
  File menu -> Preferences -> Plugins -> 'Available' section
  ```


### Add your plugin

Create PR with one new JSON file in `list` directory:
```
list/<category>/<plugin-id>.json
```
with content:
```json
{
  "locationId": "<locationId of your plugin>"
}
```
