#!/bin/bash
set -e

# Configuration
APP_NAME="CrossDocs"
APP_VERSION="1.0.0"
ARCH="x86_64"
AUTHOR_NAME="YadnyeshKolte"
AUTHOR_EMAIL="your.email@example.com"

# Directory setup
ROOT_DIR="$(pwd)"
BUILD_DIR="$ROOT_DIR/build/compose/binaries/main/app"
APPDIR="$ROOT_DIR/AppDir"
DIST_DIR="$ROOT_DIR/dist"

# Create necessary directories
mkdir -p "$DIST_DIR"
mkdir -p "$APPDIR/usr/bin"
mkdir -p "$APPDIR/usr/lib"
mkdir -p "$APPDIR/usr/share/applications"
mkdir -p "$APPDIR/usr/share/icons/hicolor/256x256/apps"
mkdir -p "$APPDIR/usr/share/metainfo"

# Build the application
echo "Building application..."
./gradlew :composeApp:packageReleaseDistributionForCurrentOS

# Create AppRun script
cat > "$APPDIR/AppRun" << 'EOF'
#!/bin/bash
HERE="$(dirname "$(readlink -f "${0}")")"
export JAVA_HOME="${HERE}/usr/lib/jvm/java-17-openjdk"
export PATH="${HERE}/usr/bin:${PATH}"
export LD_LIBRARY_PATH="${HERE}/usr/lib:${LD_LIBRARY_PATH}"
exec "${HERE}/usr/bin/CrossDocs" "$@"
EOF
chmod +x "$APPDIR/AppRun"

# Copy application files
echo "Copying application files..."
cp -r "$BUILD_DIR"/* "$APPDIR/usr/lib/"
ln -s "../lib/CrossDocs" "$APPDIR/usr/bin/CrossDocs"

# Create desktop entry
cat > "$APPDIR/usr/share/applications/crossdocs.desktop" << EOF
[Desktop Entry]
Name=CrossDocs
Exec=CrossDocs
Icon=crossdocs
Type=Application
Categories=Office;Development;
Comment=Cross-platform Markdown Editor
Terminal=false
EOF

# Create AppStream metadata
cat > "$APPDIR/usr/share/metainfo/com.yadnyeshkolte.crossdocs.appdata.xml" << EOF
<?xml version="1.0" encoding="UTF-8"?>
<component type="desktop-application">
  <id>com.yadnyeshkolte.crossdocs</id>
  <name>CrossDocs</name>
  <summary>Cross-platform Markdown Editor</summary>
  <metadata_license>MIT</metadata_license>
  <project_license>MIT</project_license>
  <description>
    <p>
      A feature-rich Markdown editor that works across platforms with live preview,
      AI assistance, and advanced formatting options.
    </p>
  </description>
  <categories>
    <category>Office</category>
    <category>Development</category>
  </categories>
  <developer_name>$AUTHOR_NAME</developer_name>
  <update_contact>$AUTHOR_EMAIL</update_contact>
  <releases>
    <release version="$APP_VERSION" date="2024-01-13"/>
  </releases>
</component>
EOF

# Copy icon
cp "$ROOT_DIR/resources/icon.png" "$APPDIR/usr/share/icons/hicolor/256x256/apps/crossdocs.png"

# Download and use appimagetool
echo "Creating AppImage..."
if [ ! -f appimagetool ]; then
    wget -O appimagetool "https://github.com/AppImage/AppImageKit/releases/download/continuous/appimagetool-x86_64.AppImage"
    chmod +x appimagetool
fi

# Generate AppImage
ARCH=$ARCH ./appimagetool "$APPDIR" "$DIST_DIR/${APP_NAME}-${APP_VERSION}-$ARCH.AppImage"

# Create checksum
cd "$DIST_DIR"
sha256sum "${APP_NAME}-${APP_VERSION}-$ARCH.AppImage" > "${APP_NAME}-${APP_VERSION}-$ARCH.AppImage.sha256"

echo "AppImage created successfully!"
echo "Your AppImage is located at: $DIST_DIR/${APP_NAME}-${APP_VERSION}-$ARCH.AppImage"
