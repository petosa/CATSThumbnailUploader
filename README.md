# CATSThumbnailUploader
Thumbnail image uploader for Georgia Tech's Center for Accessible Technology in Sign (CATS) SMARTSignDictionary
Download the latest release: https://github.com/petosa/CATSThumbnailUploader/releases

A command line interface for automatically setting large collections of JPG images as thumbnails for YouTube videos.
Uses the YouTube Data v3 API and gains access to get/set permissions via OAuth2.

Once you have granted the application get/set permissions via browser prompt, you must then use the JFileChooser dialog to select a directory that contains your JPG files. That specified folder and all its subfolders will be searched for JPGs.

The name of your JPG image must match the title of the video you would like to pair it with.
Example: The video titled My_Video on YouTube will be paired with the My_Video.jpg thumbnail.

This application is intended to be used in tandem with https://github.com/petosa/CATSImageDownloader to gather thumbnails.
