# 🖌️ Collab Draw

**Collab_draw** is a collaborative GUI drawing application developed as part of a reactive programming course. It allows multiple clients to connect to the same drawing board over a network and draw in real-time, either freely or using predefined shapes like rectangles, circles, and straight lines.

## 🧠 Features

- ✍️ **Real-time collaborative drawing** between multiple clients.
- 🟦 **Draw predefined shapes**: circles, rectangles, straight lines.
- ✏️ **Freehand drawing** support.
- 🌐 **Client-server architecture**: centralized server to manage connected clients.
- ⚡ **Reactive GUI** built using reactive programming techniques (JavaFrame).

## 📁 File Structure

- `Client.java` – Handles client-side networking and user interaction.
- `Server.java` – Manages incoming connections and synchronizes drawing events.
- `CollaborativeDrawing.java` – Entry point of the application.
- `JavaFrame.java` – Main GUI frame built using reactive programming principles.
- `DrawingPanel.java` – Custom panel for rendering shapes and handling input.
- `Drawing.java` – Manages a list of drawable objects and drawing logic.
- `Drawable.java` – Interface representing drawable objects.
- `FreeHand.java`, `Circle.java`, `Rectangle.java`, `StraightLine.java` – Implementations of various drawable shapes.
- `Point.java`, `Shape.java` – Utility classes for handling geometric data.
- `MissingEndpointException.java` – Custom exception for network handling issues.

## ⚙️ Reactive Programming
The GUI was implemented using principles of reactive programming (RxJava), primarily demonstrated in JavaFrame.java.

## 📄 Prerequisites

- Java 8 or above
- Java IDE or terminal with `javac` and `java`
- RxJava (Reactive Extensions for Java)
