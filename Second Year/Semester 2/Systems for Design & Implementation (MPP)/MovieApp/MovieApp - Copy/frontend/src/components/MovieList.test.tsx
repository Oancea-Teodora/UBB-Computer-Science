import { describe, test, expect } from "vitest";
import { render, screen, fireEvent } from "@testing-library/react";
import "@testing-library/jest-dom";
import MovieList from "./MovieList";
import React from "react";


beforeAll(() => {
    global.ResizeObserver = class {
        observe() { }
        unobserve() { }
        disconnect() { }
    };
});


describe("MovieList Component", () => {
    test("adds a movie with title, director, and date", () => {
        render(<MovieList />);

        const addButton = screen.getByText(/Add Movie/i);
        fireEvent.click(addButton);

        const titleInput = screen.getByLabelText(/Title/i);
        const directorInput = screen.getByLabelText(/^Director$/i);
        const dateInput = screen.getByLabelText(/Date/i);

        fireEvent.change(titleInput, { target: { value: "jThe Prestige" } });
        fireEvent.change(directorInput, { target: { value: "jChristopher Nolan" } });
        fireEvent.change(dateInput, { target: { value: "j20 October 2006" } });

        const saveButton = screen.getByText(/Save/i);
        fireEvent.click(saveButton);

        expect(screen.getByText("jThe Prestige")).toBeInTheDocument();
        expect(screen.getByText("jChristopher Nolan")).toBeInTheDocument();
        expect(screen.getByText("j20 October 2006")).toBeInTheDocument();
    });

    test("adds a test that doesn’t have date", () => {
        render(<MovieList />);

        const addButton = screen.getByText(/Add Movie/i);
        fireEvent.click(addButton);

        const titleInput = screen.getByLabelText(/Title/i);
        const directorInput = screen.getByLabelText(/^Director$/i);
        const dateInput = screen.getByLabelText(/Date/i);

        fireEvent.change(titleInput, { target: { value: "jj" } });
        fireEvent.change(directorInput, { target: { value: "jjj" } });
        fireEvent.change(dateInput, { target: { value: "jjjj" } });

        const saveButton = screen.getByText(/Save/i);
        fireEvent.click(saveButton);

        expect(screen.getByText("jj")).toBeInTheDocument();
        expect(screen.getByText("jjj")).toBeInTheDocument();
        expect(screen.getByText("jjjj")).toBeInTheDocument();
    });


    test("deletes 'Inception' using index", () => {
        render(<MovieList />);
        expect(screen.getByText("Inception")).toBeInTheDocument();
        const deleteIcons = screen.getAllByTestId("DeleteIcon");

        fireEvent.click(deleteIcons[5]);
        expect(screen.queryByText("Inception")).not.toBeInTheDocument();
    });

    test("updates a movie", () => {
        render(<MovieList />);

        const editIcons = screen.getAllByTestId("EditIcon");
        fireEvent.click(editIcons[1]);
        const titleInput = screen.getByLabelText(/Title/i);
        fireEvent.change(titleInput, { target: { value: "Inception Updated" } });
        const saveButton = screen.getByText(/Save/i);
        fireEvent.click(saveButton);
        expect(screen.getByText("Inception Updated")).toBeInTheDocument();
    });


    test("filters movies by director", () => {
        render(<MovieList />);

        const searchField = screen.getByLabelText(/Search by Director/i);
        fireEvent.change(searchField, { target: { value: "Fincher" } });

        expect(screen.getByText("Fight Club")).toBeInTheDocument();
        expect(screen.queryByText("Inception")).not.toBeInTheDocument();
    });

});
