import os
import sys
import cv2
import face_recognition
import numpy as np
from ultralytics import YOLO

def load_owner_encodings(known_dir='./known_faces/owner'):
    encodings = []
    if not os.path.isdir(known_dir):
        print(f"No known faces directory found at {known_dir}. Exiting.")
        sys.exit(1)
    for fname in os.listdir(known_dir):
        fpath = os.path.join(known_dir, fname)
        img = face_recognition.load_image_file(fpath)
        face_locs = face_recognition.face_locations(img)
        if not face_locs:
            continue
        enc = face_recognition.face_encodings(img, face_locs)[0]
        encodings.append(enc)
    return encodings


def classify_frame(frame, owner_encodings, pet_model, tolerance=0.5):
    # Resize for speed
    small_frame = cv2.resize(frame, (0, 0), fx=0.5, fy=0.5)
    rgb_small = small_frame[:, :, ::-1]

    face_locs = face_recognition.face_locations(rgb_small)

    # Manually crop & encode each face from the full-res frame
    face_encs = []
    for (top, right, bottom, left) in face_locs:
        # scale box coords back up to full frame size
        top    *= 2
        right  *= 2
        bottom *= 2
        left   *= 2

        # crop & convert to RGB
        face_img = frame[top:bottom, left:right]
        rgb_face = cv2.cvtColor(face_img, cv2.COLOR_BGR2RGB)

        # get the encoding
        encs = face_recognition.face_encodings(rgb_face)
        if encs:
            face_encs.append(encs[0])

    # now compare
    for enc in face_encs:
        if any(face_recognition.compare_faces(owner_encodings, enc, tolerance)):
            return "Owner"
    if face_encs:
        return "Other Person"


    # Pet detection
    results = pet_model.predict(frame)
    for r in results:
        for box, cls in zip(r.boxes.xyxy, r.boxes.cls):
            name = pet_model.names[int(cls)]
            if name in ['dog', 'cat']:
                return "Pet"

    return "Nobody"


def main():
    owner_encodings = load_owner_encodings()
    print(f"Loaded {len(owner_encodings)} owner face encoding(s).")

    # Load pet detection model (YOLO)
    pet_model = YOLO('yolov8n.pt')  # ensure model file exists

    # Start webcam
    cap = cv2.VideoCapture(0)
    if not cap.isOpened():
        print("Cannot open webcam. Exiting.")
        sys.exit(1)

    print("Press 'q' to quit.")
    while True:
        ret, frame = cap.read()
        if not ret:
            break

        label = classify_frame(frame, owner_encodings, pet_model)
        # Display label
        cv2.putText(frame, f"Status: {label}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0), 2)
        cv2.imshow('AI Classification', frame)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()


if __name__ == '__main__':
    main()
