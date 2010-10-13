-- Disable the screen Saver Password
do shell script "defaults -currentHost write com.apple.screensaver askForPassword -int 0"
do shell script "notif"
-- Turn OFF the screen saver
tell application "ScreenSaverEngine" to quit
tell application "Address Book"
     if not unsaved then
          try
               quit
               delay 1
          end try
     end if
end tell
-- Reconnect to the Address Book
do shell script "defaults write com.apple.AddressBook ABCheckForPhoneNextTime -boolean true"
try
     tell application "Address Book"
          launch
     end tell
     tell application "System Events"
          set the visible of process "Address Book" to no
     end tell
end try
-- Synchronize the Device
tell application "iSync"
     if last sync is less than ((current date) - 900) then
          synchronize
     end if
end tell
tell application "System Events" to set visible of process "iSync" to false